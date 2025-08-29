// Simple frontend logic for switching sections and adding demo items
function showSection(section) {
    document.getElementById('grade').style.display = 'none';
    document.getElementById('assignment').style.display = 'none';
    document.getElementById('lecture').style.display = 'none';
    document.getElementById(section).style.display = 'block';
}

// Demo data for frontend only
let terms = [];
let assignments = [];
let lectures = [];

function addTerm() {
    const name = prompt('Enter term name (e.g. 2025 Semester 1):');
    if (name) {
        terms.push({ name, courses: [] });
        renderTerms();
    }
}

function renderTerms() {
    const container = document.querySelector('.term-list');
    container.innerHTML = '';
    terms.forEach((term, idx) => {
        const card = document.createElement('div');
        card.className = 'card';
        card.innerHTML = `<div class='card-title'>${term.name}</div>
            <div class='card-details'>Courses: ${term.courses.length}</div>
            <button onclick='addCourse(${idx})'>Add Course</button>`;
        container.appendChild(card);
    });
}

function addCourse(termIdx) {
    const name = prompt('Enter course name (e.g. SWEN225):');
    if (name) {
        terms[termIdx].courses.push({ name, assignments: [] });
        renderTerms();
    }
}

function addAssignment() {
    const name = prompt('Assignment name:');
    if (name) {
        assignments.push({ name, type: 'Homework', priority: 'Medium', status: 'Not Started', progress: 0, daysLeft: 7 });
        renderAssignments();
    }
}

function renderAssignments() {
    const container = document.querySelector('.assignment-list');
    container.innerHTML = '';
    assignments.forEach((a) => {
        const card = document.createElement('div');
        card.className = 'card';
        card.innerHTML = `<div class='card-title'>${a.name}</div>
            <div class='card-details'>Type: ${a.type} | Priority: ${a.priority} | Status: ${a.status} | Progress: ${a.progress}% | Days Left: ${a.daysLeft}</div>`;
        container.appendChild(card);
    });
}

function addLecture() {
    const name = prompt('Lecture name:');
    if (name) {
        lectures.push({ name, attended: false });
        renderLectures();
    }
}

function renderLectures() {
    const container = document.querySelector('.lecture-list');
    container.innerHTML = '';
    lectures.forEach((l, idx) => {
        const card = document.createElement('div');
        card.className = 'card';
        card.innerHTML = `<div class='card-title'>${l.name}</div>
            <div class='card-details'>Attended: <input type='checkbox' ${l.attended ? 'checked' : ''} onchange='toggleLecture(${idx})'></div>`;
        container.appendChild(card);
    });
}

function toggleLecture(idx) {
    lectures[idx].attended = !lectures[idx].attended;
    renderLectures();
}

// Initial render
renderTerms();
renderAssignments();
renderLectures();
