function showStudentForm() {

    document.getElementById("content").innerHTML = `

        <div class="card">

            <h2>Student Registration</h2>

            <input
                type="text"
                id="name"
                placeholder="Student Name">

            <input
                type="email"
                id="email"
                placeholder="Email">

            <input
                type="text"
                id="phone"
                placeholder="Phone Number">

            <input
                type="text"
                id="collegeName"
                placeholder="College Name">

            <button onclick="registerStudent()">
                Continue
            </button>

        </div>
    `;
}


async function registerStudent() {

    const student = {

        name: document.getElementById("name").value,

        email: document.getElementById("email").value,

        phone: document.getElementById("phone").value,

        collegeName: document.getElementById("collegeName").value
    };


    if (
        !student.name ||
        !student.email ||
        !student.phone ||
        !student.collegeName
    ) {

        alert("Please fill all student details");

        return;
    }


    try {

        const response = await fetch(
            "/api/students",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(student)
            }
        );


        const data = await response.json();


        if (!response.ok) {

            alert(data.message || "Registration failed");

            return;
        }


        alert("Student registered successfully!");

        showApplicationForm(data.id);

    } catch (error) {

        alert("Unable to connect to server");

        console.error(error);
    }
}


async function showApplicationForm(studentId) {

    try {

        const response = await fetch("/api/routes");

        const routes = await response.json();


        let routeOptions = "";

        routes.forEach(route => {

            routeOptions += `
                <option value="${route.id}">
                    ${route.routeName}
                    -
                    ${route.boardingPoint}
                    to
                    ${route.destination}
                </option>
            `;
        });


        document.getElementById("content").innerHTML = `

            <div class="card">

                <h2>Bus Pass Application</h2>

                <input
                    type="hidden"
                    id="studentId"
                    value="${studentId}">

                <label>Bus Route</label>

                <select id="routeId">

                    <option value="">
                        Select Route
                    </option>

                    ${routeOptions}

                </select>

                <input
                    type="text"
                    id="boardingPoint"
                    placeholder="Boarding Point">

                <label>Student Photo</label>

                <input
                    type="file"
                    id="studentPhoto"
                    accept="image/*">

                <button onclick="submitApplication()">
                    Submit Application
                </button>

            </div>
        `;

    } catch (error) {

        alert("Unable to load bus routes");

        console.error(error);
    }
}



async function submitApplication() {

    const studentId =
        document.getElementById("studentId").value;

    const routeId =
        document.getElementById("routeId").value;

    const boardingPoint =
        document.getElementById("boardingPoint").value;

    const photoFile =
        document.getElementById("studentPhoto").files[0];

    if (!routeId) {
        alert("Please select a bus route");
        return;
    }

    if (!boardingPoint.trim()) {
        alert("Please enter boarding point");
        return;
    }

    if (!photoFile) {
        alert("Please upload student photo");
        return;
    }

    if (!photoFile.type.startsWith("image/")) {
        alert("Please select a valid image");
        return;
    }

    const formData = new FormData();

    formData.append("studentId", studentId);
    formData.append("routeId", routeId);
    formData.append("boardingPoint", boardingPoint);
    formData.append("photo", photoFile);

    try {

        const response = await fetch(
            "/api/applications",
            {
                method: "POST",
                body: formData
            }
        );

        const data = await response.json();

        if (!response.ok) {
            alert(data.message || "Application failed");
            return;
        }

        document.getElementById("content").innerHTML = `
            <div class="card">
                <h2>Application Submitted Successfully!</h2>

                <p>
                    <strong>Application ID:</strong>
                    ${data.id}
                </p>

                <p>
                    <strong>Status:</strong>
                    ${data.status}
                </p>

                <p>
                    <strong>Photo:</strong>
                    ${data.photoReference}
                </p>

                <p>
                    Your application is waiting for admin approval.
                </p>

                <button onclick="showStatusForm()">
                    Check Application Status
                </button>
            </div>
        `;

    } catch (error) {
        alert("Unable to connect to server");
        console.error(error);
    }
}

async function checkApplicationStatus() {

    const applicationId =
        document.getElementById("applicationId").value;


    if (!applicationId) {

        alert("Please enter Application ID");

        return;
    }


    try {

        const response = await fetch(
            `/api/applications/${applicationId}`
        );


        const data = await response.json();


        if (!response.ok) {

            alert(data.message || "Application not found");

            return;
        }


        let html = `

            <div class="card">

                <h2>Application Status</h2>

                <p>
                    <strong>Application ID:</strong>
                    ${data.id}
                </p>

                <p>
                    <strong>Student:</strong>
                    ${data.student.name}
                </p>

                <p>
                    <strong>Route:</strong>
                    ${data.busRoute.routeName}
                </p>

                <p>
                    <strong>Boarding Point:</strong>
                    ${data.boardingPoint}
                </p>

                <p>
                    <strong>Status:</strong>
                    ${data.status}
                </p>
        `;


        if (data.status === "APPROVED") {

            html += `

                <p>
                    <strong>Pass Number:</strong>
                    ${data.passNumber}
                </p>

                <p>
                    <strong>Valid From:</strong>
                    ${data.validFrom}
                </p>

                <p>
                    <strong>Valid Until:</strong>
                    ${data.validUntil}
                </p>
            `;
        }


        if (data.status === "REJECTED") {

            html += `

                <p>
                    <strong>Rejection Reason:</strong>
                    ${data.rejectionReason}
                </p>
            `;
        }


        if (data.status === "PENDING") {

            html += `

                <p>
                    Your application is waiting
                    for admin approval.
                </p>
            `;
        }


        html += `</div>`;


        document.getElementById("content").innerHTML =
            html;

    } catch (error) {

        alert("Unable to connect to server");

        console.error(error);
    }
}


async function showApplications() {

    try {

        const response =
            await fetch("/api/applications");


        const applications =
            await response.json();


        let html = `

            <div class="card">

                <h2>Admin - Applications</h2>
        `;


        if (applications.length === 0) {

            html += `
                <p>No applications found.</p>
            `;

        } else {

            applications.forEach(app => {

                html += `

                    <div class="application">

                        <p>
                            <strong>
                                Application ID:
                            </strong>
                            ${app.id}
                        </p>

                        <p>
                            <strong>
                                Student:
                            </strong>
                            ${app.student.name}
                        </p>

                        <p>
                            <strong>
                                Route:
                            </strong>
                            ${app.busRoute.routeName}
                        </p>

                        <p>
                            <strong>
                                Boarding Point:
                            </strong>
                            ${app.boardingPoint}
                        </p>

                        <p>
                            <strong>
                                Status:
                            </strong>
                            ${app.status}
                        </p>
                `;


                if (app.status === "PENDING") {

                    html += `

                        <button
                            onclick="approveApplication(${app.id})">
                            Approve
                        </button>

                        <button
                            onclick="rejectApplication(${app.id})">
                            Reject
                        </button>
                    `;
                }


                if (app.status === "APPROVED") {

                    html += `

                        <p>
                            <strong>
                                Pass Number:
                            </strong>
                            ${app.passNumber}
                        </p>

                        <p>
                            <strong>
                                Valid Until:
                            </strong>
                            ${app.validUntil}
                        </p>
                    `;
                }


                if (app.status === "REJECTED") {

                    html += `

                        <p>
                            <strong>
                                Rejection Reason:
                            </strong>
                            ${app.rejectionReason}
                        </p>
                    `;
                }


                html += `</div>`;
            });
        }


        html += `</div>`;


        document.getElementById("content").innerHTML =
            html;

    } catch (error) {

        alert("Unable to load applications");

        console.error(error);
    }
}


async function approveApplication(applicationId) {

    try {

        const response = await fetch(
            `/api/applications/${applicationId}/approve`,
            {
                method: "PUT"
            }
        );


        const data = await response.json();


        if (!response.ok) {

            alert(data.message || "Approval failed");

            return;
        }


        alert(
            "Application approved!\n" +
            "Pass Number: " +
            data.passNumber
        );


        showApplications();

    } catch (error) {

        alert("Unable to connect to server");

        console.error(error);
    }
}


async function rejectApplication(applicationId) {

    const reason = prompt(
        "Enter rejection reason:"
    );


    if (!reason || reason.trim() === "") {

        alert("Rejection reason is required");

        return;
    }


    try {

        const response = await fetch(
            `/api/applications/${applicationId}/reject?reason=${encodeURIComponent(reason)}`,
            {
                method: "PUT"
            }
        );


        const data = await response.json();


        if (!response.ok) {

            alert(data.message || "Rejection failed");

            return;
        }


        alert("Application rejected");


        showApplications();

    } catch (error) {

        alert("Unable to connect to server");

        console.error(error);
    }
}