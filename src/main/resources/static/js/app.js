/* ==================================================
   SeatPlan — Frontend Single Page Application Logic
   ================================================== */

const API_BASE = '/api';
let currentExamForRegistration = null;
let allStudentsListCache = [];

document.addEventListener('DOMContentLoaded', () => {
    initNavigation();
    refreshDashboard();
    loadHalls();
    loadStudents();
    loadExams();
});

// Navigation Tab Switching
function initNavigation() {
    const tabBtns = document.querySelectorAll('.tab-btn');
    tabBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            const tabId = btn.getAttribute('data-tab');
            switchTab(tabId);
        });
    });
}

function switchTab(tabId) {
    document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
    document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));

    const activeBtn = document.querySelector(`.tab-btn[data-tab="${tabId}"]`);
    const activeContent = document.getElementById(`tab-${tabId}`);

    if (activeBtn) activeBtn.classList.add('active');
    if (activeContent) activeContent.classList.add('active');

    if (tabId === 'dashboard') refreshDashboard();
    if (tabId === 'halls') loadHalls();
    if (tabId === 'students') loadStudents();
    if (tabId === 'exams') loadExams();
    if (tabId === 'seating') populateExamDropdowns();
    if (tabId === 'lookup') populateExamDropdowns();
}

// Toast Notifications
function showToast(message, type = 'info') {
    const container = document.getElementById('toastContainer');
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    
    let iconClass = 'fa-info-circle';
    if (type === 'success') iconClass = 'fa-circle-check';
    if (type === 'error') iconClass = 'fa-triangle-exclamation';

    toast.innerHTML = `<i class="fa-solid ${iconClass}"></i> <span>${escapeHtml(message)}</span>`;
    container.appendChild(toast);

    setTimeout(() => {
        toast.style.opacity = '0';
        setTimeout(() => toast.remove(), 300);
    }, 4000);
}

// 1. DASHBOARD
async function refreshDashboard() {
    try {
        const [hallsRes, studentsRes, examsRes] = await Promise.all([
            fetch(`${API_BASE}/halls`).then(r => r.json()),
            fetch(`${API_BASE}/students`).then(r => r.json()),
            fetch(`${API_BASE}/exams`).then(r => r.json())
        ]);

        const totalHalls = hallsRes.length || 0;
        const totalSeats = hallsRes.reduce((acc, h) => acc + (h.capacity || 0), 0);
        const totalStudents = studentsRes.length || 0;
        const totalExams = examsRes.length || 0;

        document.getElementById('statHalls').innerText = totalHalls;
        document.getElementById('statSeats').innerText = totalSeats;
        document.getElementById('statStudents').innerText = totalStudents;
        document.getElementById('statExams').innerText = totalExams;
    } catch (err) {
        console.error('Failed to load dashboard stats:', err);
    }
}

// 2. HALLS MANAGEMENT
async function loadHalls() {
    try {
        const res = await fetch(`${API_BASE}/halls`);
        const halls = await res.json();
        const tbody = document.getElementById('hallsTableBody');

        if (!halls || halls.length === 0) {
            tbody.innerHTML = `<tr><td colspan="5" class="text-center text-muted">No halls registered yet.</td></tr>`;
            return;
        }

        tbody.innerHTML = halls.map(h => `
            <tr>
                <td><strong>${escapeHtml(h.hallNumber)}</strong></td>
                <td>${escapeHtml(h.name)}</td>
                <td>${h.rows} × ${h.columns}</td>
                <td><span class="badge sec-csea">${h.capacity} seats</span></td>
                <td>
                    <button class="btn btn-danger btn-sm" onclick="deleteHall(${h.id})">
                        <i class="fa-solid fa-trash"></i>
                    </button>
                </td>
            </tr>
        `).join('');
    } catch (err) {
        showToast('Error loading halls', 'error');
    }
}

function calculateCapacityPreview() {
    const rows = parseInt(document.getElementById('hallRows').value) || 0;
    const cols = parseInt(document.getElementById('hallCols').value) || 0;
    if (rows > 0 && cols > 0) {
        document.getElementById('hallCapacity').value = rows * cols;
    }
}

async function handleCreateHall(e) {
    e.preventDefault();
    const payload = {
        hallNumber: document.getElementById('hallNumber').value.trim(),
        name: document.getElementById('hallName').value.trim(),
        rows: parseInt(document.getElementById('hallRows').value),
        columns: parseInt(document.getElementById('hallCols').value),
        capacity: parseInt(document.getElementById('hallCapacity').value)
    };

    try {
        const res = await fetch(`${API_BASE}/halls`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (!res.ok) {
            const errData = await res.json();
            throw new Error(errData.message || 'Failed to create hall');
        }

        showToast('Hall created & seats auto-generated successfully!', 'success');
        document.getElementById('hallForm').reset();
        loadHalls();
        refreshDashboard();
    } catch (err) {
        showToast(err.message, 'error');
    }
}

async function deleteHall(id) {
    if (!confirm('Are you sure you want to delete this hall?')) return;
    try {
        const res = await fetch(`${API_BASE}/halls/${id}`, { method: 'DELETE' });
        if (res.ok) {
            showToast('Hall deleted successfully', 'success');
            loadHalls();
            refreshDashboard();
        } else {
            showToast('Failed to delete hall', 'error');
        }
    } catch (err) {
        showToast(err.message, 'error');
    }
}

// 3. STUDENTS MANAGEMENT
async function loadStudents() {
    try {
        const res = await fetch(`${API_BASE}/students`);
        const students = await res.json();
        allStudentsListCache = students;
        const tbody = document.getElementById('studentsTableBody');

        if (!students || students.length === 0) {
            tbody.innerHTML = `<tr><td colspan="5" class="text-center text-muted">No students registered yet.</td></tr>`;
            return;
        }

        tbody.innerHTML = students.map(s => {
            const sectionClass = getSectionBadgeClass(s.className, s.section);
            return `
                <tr>
                    <td><strong>${escapeHtml(s.rollNumber)}</strong></td>
                    <td>${escapeHtml(s.name)}</td>
                    <td>${escapeHtml(s.className)}</td>
                    <td><span class="badge ${sectionClass}">${escapeHtml(s.section)}</span></td>
                    <td>
                        <button class="btn btn-danger btn-sm" onclick="deleteStudent(${s.id})">
                            <i class="fa-solid fa-trash"></i>
                        </button>
                    </td>
                </tr>
            `;
        }).join('');
    } catch (err) {
        showToast('Error loading students', 'error');
    }
}

async function handleCreateStudent(e) {
    e.preventDefault();
    const payload = {
        rollNumber: document.getElementById('rollNumber').value.trim(),
        name: document.getElementById('studentName').value.trim(),
        className: document.getElementById('className').value.trim().toUpperCase(),
        section: document.getElementById('section').value.trim().toUpperCase()
    };

    try {
        const res = await fetch(`${API_BASE}/students`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (!res.ok) {
            const errData = await res.json();
            throw new Error(errData.message || 'Failed to register student');
        }

        showToast('Student registered successfully!', 'success');
        document.getElementById('studentForm').reset();
        loadStudents();
        refreshDashboard();
    } catch (err) {
        showToast(err.message, 'error');
    }
}

async function deleteStudent(id) {
    if (!confirm('Are you sure you want to delete this student?')) return;
    try {
        const res = await fetch(`${API_BASE}/students/${id}`, { method: 'DELETE' });
        if (res.ok) {
            showToast('Student deleted successfully', 'success');
            loadStudents();
            refreshDashboard();
        } else {
            showToast('Failed to delete student', 'error');
        }
    } catch (err) {
        showToast(err.message, 'error');
    }
}

// 4. EXAM SESSIONS
async function loadExams() {
    try {
        const res = await fetch(`${API_BASE}/exams`);
        const exams = await res.json();
        const tbody = document.getElementById('examsTableBody');

        if (!exams || exams.length === 0) {
            tbody.innerHTML = `<tr><td colspan="6" class="text-center text-muted">No exam sessions created yet.</td></tr>`;
            return;
        }

        tbody.innerHTML = exams.map(ex => `
            <tr>
                <td>#${ex.id}</td>
                <td><strong>${escapeHtml(ex.examName)}</strong></td>
                <td>${escapeHtml(ex.subject)}</td>
                <td>${ex.examDate} (${ex.startTime} - ${ex.endTime})</td>
                <td><span class="badge sec-csea">${ex.registeredStudentsCount} enrolled</span></td>
                <td>
                    <button class="btn btn-secondary btn-sm" onclick="openRegisterPanel(${ex.id})">
                        <i class="fa-solid fa-user-plus"></i> Register Students
                    </button>
                    <button class="btn btn-danger btn-sm" onclick="deleteExam(${ex.id})">
                        <i class="fa-solid fa-trash"></i>
                    </button>
                </td>
            </tr>
        `).join('');

        populateExamDropdowns();
    } catch (err) {
        showToast('Error loading exam sessions', 'error');
    }
}

async function handleCreateExam(e) {
    e.preventDefault();
    const payload = {
        examName: document.getElementById('examName').value.trim(),
        subject: document.getElementById('examSubject').value.trim(),
        examDate: document.getElementById('examDate').value,
        startTime: document.getElementById('startTime').value,
        endTime: document.getElementById('endTime').value
    };

    try {
        const res = await fetch(`${API_BASE}/exams`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (!res.ok) {
            const errData = await res.json();
            throw new Error(errData.message || 'Failed to create exam session');
        }

        showToast('Exam session created successfully!', 'success');
        document.getElementById('examForm').reset();
        loadExams();
        refreshDashboard();
    } catch (err) {
        showToast(err.message, 'error');
    }
}

async function deleteExam(id) {
    if (!confirm('Are you sure you want to delete this exam session?')) return;
    try {
        const res = await fetch(`${API_BASE}/exams/${id}`, { method: 'DELETE' });
        if (res.ok) {
            showToast('Exam session deleted successfully', 'success');
            loadExams();
            refreshDashboard();
        } else {
            showToast('Failed to delete exam session', 'error');
        }
    } catch (err) {
        showToast(err.message, 'error');
    }
}

// Register Students Panel inside Exam Session Tab
async function openRegisterPanel(examId) {
    currentExamForRegistration = examId;
    document.getElementById('selectedExamIdDisplay').innerText = examId;

    const panel = document.getElementById('registerStudentsPanel');
    const container = document.getElementById('registrationStudentCheckboxes');

    if (allStudentsListCache.length === 0) {
        const res = await fetch(`${API_BASE}/students`);
        allStudentsListCache = await res.json();
    }

    const registeredRes = await fetch(`${API_BASE}/exams/${examId}/students`);
    const registeredStudents = await registeredRes.json();
    const registeredSet = new Set(registeredStudents.map(s => s.id));

    container.innerHTML = allStudentsListCache.map(s => `
        <label class="student-checkbox-item">
            <input type="checkbox" value="${s.id}" ${registeredSet.has(s.id) ? 'checked' : ''}>
            <span><strong>${escapeHtml(s.rollNumber)}</strong> - ${escapeHtml(s.name)} (${s.className}-${s.section})</span>
        </label>
    `).join('');

    panel.style.display = 'block';
}

function closeRegisterPanel() {
    document.getElementById('registerStudentsPanel').style.display = 'none';
}

function selectAllStudentsToRegister(check) {
    const checkboxes = document.querySelectorAll('#registrationStudentCheckboxes input[type="checkbox"]');
    checkboxes.forEach(cb => cb.checked = check);
}

async function submitExamRegistration() {
    if (!currentExamForRegistration) return;
    const selectedIds = Array.from(document.querySelectorAll('#registrationStudentCheckboxes input[type="checkbox"]:checked'))
        .map(cb => parseInt(cb.value));

    try {
        const res = await fetch(`${API_BASE}/exams/${currentExamForRegistration}/students`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ studentIds: selectedIds })
        });

        if (!res.ok) {
            const errData = await res.json();
            throw new Error(errData.message || 'Registration failed');
        }

        showToast('Students registered for exam session successfully!', 'success');
        closeRegisterPanel();
        loadExams();
    } catch (err) {
        showToast(err.message, 'error');
    }
}

async function populateExamDropdowns() {
    try {
        const res = await fetch(`${API_BASE}/exams`);
        const exams = await res.json();

        const optionsHtml = exams.length > 0 ?
            exams.map(e => `<option value="${e.id}">#${e.id} - ${escapeHtml(e.examName)} (${escapeHtml(e.subject)})</option>`).join('') :
            `<option value="">No Exam Sessions Available</option>`;

        document.getElementById('seatingExamSelect').innerHTML = optionsHtml;
        document.getElementById('lookupExamSelect').innerHTML = optionsHtml;

        if (exams.length > 0) {
            loadSeatingChart(exams[0].id);
        }
    } catch (err) {
        console.error('Failed to populate exam dropdowns:', err);
    }
}

// 5. SEATING ARRANGEMENT GENERATOR & SEATING CHART
function handleExamSelectChange() {
    const examId = document.getElementById('seatingExamSelect').value;
    if (examId) loadSeatingChart(examId);
}

async function triggerGenerateSeating() {
    const examId = document.getElementById('seatingExamSelect').value;
    if (!examId) {
        showToast('Please select an exam session first.', 'error');
        return;
    }

    try {
        const res = await fetch(`${API_BASE}/exams/${examId}/generate-seating`, { method: 'POST' });
        const data = await res.json();

        if (!res.ok) {
            throw new Error(data.message || 'Seating generation failed');
        }

        showAlertStatus(data.message, data.constraintFullySatisfied ? 'success' : 'warning');
        showToast('Seating arrangement generated successfully!', 'success');
        loadSeatingChart(examId);
    } catch (err) {
        showAlertStatus(err.message, 'error');
        showToast(err.message, 'error');
    }
}

async function triggerRegenerateSeating() {
    const examId = document.getElementById('seatingExamSelect').value;
    if (!examId) return;

    try {
        const res = await fetch(`${API_BASE}/exams/${examId}/regenerate-seating`, { method: 'POST' });
        const data = await res.json();

        if (!res.ok) throw new Error(data.message || 'Regeneration failed');

        showAlertStatus('Seating arrangement regenerated!', 'success');
        showToast('Seating arrangement regenerated!', 'success');
        loadSeatingChart(examId);
    } catch (err) {
        showAlertStatus(err.message, 'error');
        showToast(err.message, 'error');
    }
}

async function loadSeatingChart(examId) {
    if (!examId) return;
    try {
        const res = await fetch(`${API_BASE}/exams/${examId}/seating-chart`);
        if (!res.ok) return;
        const chartData = await res.json();
        renderSeatingChart(chartData);
    } catch (err) {
        console.error('Failed to load seating chart:', err);
    }
}

function renderSeatingChart(chartData) {
    const wrapper = document.getElementById('seatingChartContainer');
    if (!chartData || !chartData.halls || chartData.halls.length === 0) {
        wrapper.innerHTML = `
            <div class="placeholder-box">
                <i class="fa-solid fa-border-all placeholder-icon"></i>
                <h3>No Seating Data Available</h3>
                <p>Click "Generate Seating" to run the seating allocation algorithm.</p>
            </div>
        `;
        return;
    }

    wrapper.innerHTML = chartData.halls.map(hall => {
        const matrixRowsHtml = hall.rows.map((rowCells, rIdx) => {
            const seatsHtml = rowCells.map(cell => {
                if (!cell.student) {
                    return `
                        <div class="seat-box vacant">
                            <span class="seat-number">${escapeHtml(cell.seatNumber)}</span>
                            <span class="seat-roll">VACANT</span>
                        </div>
                    `;
                }
                const badgeClass = getSectionBadgeClass(cell.className, cell.section);
                return `
                    <div class="seat-box ${badgeClass}">
                        <div class="seat-top">
                            <span class="seat-number">${escapeHtml(cell.seatNumber)}</span>
                            <span class="badge ${badgeClass}">${escapeHtml(cell.className)}-${escapeHtml(cell.section)}</span>
                        </div>
                        <div class="seat-student" title="${escapeHtml(cell.student)}">${escapeHtml(cell.student)}</div>
                        <div class="seat-roll">${escapeHtml(cell.rollNumber)}</div>
                    </div>
                `;
            }).join('');

            return `
                <div class="matrix-row">
                    <span class="row-label">Row ${rIdx + 1}</span>
                    ${seatsHtml}
                </div>
            `;
        }).join('');

        return `
            <div class="hall-chart-card">
                <div class="hall-chart-header">
                    <div>
                        <h3><i class="fa-solid fa-building-columns"></i> ${escapeHtml(hall.hallNumber)} - ${escapeHtml(hall.hallName)}</h3>
                        <p class="text-muted">Grid Dimensions: ${hall.totalRows} × ${hall.totalColumns} (${hall.capacity} total seats)</p>
                    </div>
                    <span class="badge sec-csea">Hall Capacity: ${hall.capacity}</span>
                </div>
                <div class="hall-matrix">
                    ${matrixRowsHtml}
                </div>
            </div>
        `;
    }).join('');
}

function triggerPrintChart() {
    window.print();
}

function showAlertStatus(message, type) {
    const alert = document.getElementById('seatingStatusAlert');
    alert.className = `alert-box ${type}`;
    alert.innerHTML = `<i class="fa-solid fa-info-circle"></i> <span>${escapeHtml(message)}</span>`;
    alert.style.display = 'flex';
}

// 6. STUDENT SEAT LOOKUP TOOL
async function handleLookupStudent(e) {
    e.preventDefault();
    const rollNumber = document.getElementById('lookupRollNumber').value.trim();
    const examId = document.getElementById('lookupExamSelect').value;
    const container = document.getElementById('lookupResultContainer');

    if (!rollNumber || !examId) return;

    try {
        const res = await fetch(`${API_BASE}/students/${encodeURIComponent(rollNumber)}/allocation/${examId}`);
        if (!res.ok) {
            const errData = await res.json();
            container.innerHTML = `
                <div class="alert-box error" style="margin-top: 2rem;">
                    <i class="fa-solid fa-circle-xmark"></i> <span>${escapeHtml(errData.message || 'Student seating allocation not found.')}</span>
                </div>
            `;
            return;
        }

        const data = await res.json();
        container.innerHTML = `
            <div class="ticket-card">
                <div class="ticket-header">
                    <div>
                        <span class="badge sec-csea">EXAMINATION ADMISSION TICKET</span>
                        <h3 style="margin-top: 0.5rem;">${escapeHtml(data.exam)}</h3>
                    </div>
                    <i class="fa-solid fa-qrcode" style="font-size: 3rem; color: var(--accent-cyan);"></i>
                </div>
                <div class="ticket-details">
                    <div class="ticket-field">
                        <label>Student Roll Number</label>
                        <span>${escapeHtml(data.rollNumber)}</span>
                    </div>
                    <div class="ticket-field">
                        <label>Student Name</label>
                        <span>${escapeHtml(data.studentName)}</span>
                    </div>
                    <div class="ticket-field highlight">
                        <label>Allocated Hall</label>
                        <span>${escapeHtml(data.hallNumber)}</span>
                    </div>
                    <div class="ticket-field highlight">
                        <label>Seat Number</label>
                        <span>${escapeHtml(data.seatNumber)}</span>
                    </div>
                    <div class="ticket-field">
                        <label>Row Position</label>
                        <span>Row ${data.row}</span>
                    </div>
                    <div class="ticket-field">
                        <label>Column Position</label>
                        <span>Column ${data.column}</span>
                    </div>
                </div>
            </div>
        `;
    } catch (err) {
        showToast(err.message, 'error');
    }
}

// Helper Utilities
function getSectionBadgeClass(className, section) {
    const key = `${className}-${section}`.toUpperCase();
    if (key.includes('CS') && key.includes('A')) return 'sec-csea';
    if (key.includes('CS') && key.includes('B')) return 'sec-cseb';
    if (key.includes('EC') && key.includes('A')) return 'sec-ecea';
    if (key.includes('EC') && key.includes('B')) return 'sec-eceb';
    if (key.includes('EE')) return 'sec-eeea';
    return 'sec-default';
}

function escapeHtml(str) {
    if (!str) return '';
    return String(str)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}
