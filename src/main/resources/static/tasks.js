/*console.log("tasks.js loaded successfully");*/
let editTaskId = null;
let allTasks = [];

/*Load employees into dropdown*/
function loadEmployees() {
    /*console.log("called loadEmp")*/
    fetch("http://localhost:8080/employees")
        .then(response => {
            console.log(response.status);
            return response.json()
        })
        .then(data => {
            /*  console.log(data)*/
            const dropdown = document.querySelector("#employeeId");

            data.forEach(employee => {
                dropdown.innerHTML += `
			<option value="${employee.id}">
			${employee.name} (ID: ${employee.id})
			</option>`;
            });
        }).catch(err => console.log("Error: ", err));
};

/*Load and display all tasks*/
function loadTasks() {
    /*console.log("called loadtasks")*/
    fetch("http://localhost:8080/tasks")
        .then(response => {
            console.log("Tasks API status:", response.status);
            return response.json();
        })
        .then(data => {
            /* console.log("Tasks received:", data);*/

            allTasks = data;
			console.log("Tasks loaded:", allTasks.length);
            renderTasks();
        }).catch(err => console.log("Error :", err))
}


/*rendering/filtering tasks*/
function renderTasks() {
	
    const tableBody = document.querySelector("#taskTableBody");
    const selectedStatus = document.querySelector("#taskStatusFilter").value;

    tableBody.innerHTML = "";

    const filteredTasks = allTasks.filter(task => {
        const status = task.status.trim().toLowerCase().replace(/[\s-]+/g, "_");

        return selectedStatus === "all" || status === selectedStatus;
    });

    filteredTasks.forEach(task => {
        const normalizedStatus = task.status.trim().toLowerCase().replace(/[\s-]+/g, "_");
        tableBody.innerHTML +=
            `<tr>
					<td>${task.title.charAt(0).toUpperCase() + task.title.slice(1)}</td>
					<td>${task.description.charAt(0).toUpperCase() + task.description.slice(1)}</td>
					<td><span class="task-status ${normalizedStatus}"> ${task.status.replace(/_/g, " ")}</span></td>
					<td>${task.employee ? task.employee.id : "Unassigned"}</td>
					<td>
						<button class="btn-edit" onclick="editTask(${task.id})">Edit</button>
						<button class="btn-primary" onclick="changeStatus(${task.id},'${task.status}')">Change Status</button>
						<button class="btn-delete" onclick="deleteTask(${task.id})">Delete</button>
					</td>
				</tr>`;
    });
}


/*Create or update a task*/
document.querySelector("#taskForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const task = {
        title: document.querySelector("#title").value,
        description: document.querySelector("#description").value,
        status: document.querySelector("#status").value,
        employeeId: document.querySelector("#employeeId").value,
    };

    let url = "http://localhost:8080/tasks";
    let method = "POST";

    if (editTaskId != null) {
        url = `http://localhost:8080/tasks/${editTaskId}`;
        method = "PUT";
    }
    fetch(url, {
        method: method,
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(task)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Could not save task. Check the data.")
            }
        }).then(data => {
            const wasEditing = editTaskId !== null;

            document.querySelector("#taskForm").reset();
            editTaskId = null;
            document.querySelector("#formTitle").textContent = "Create New Task";
            document.querySelector("#cancelEdit").style.display = "none";
            loadTasks();

            showMessage(
                "Success",
                wasEditing
                    ? "Task created successfully!"
                    : "Task updated successfully!"
            );
        })
        .catch(error => showMessage("Error", error.message));
});

/*Load a task into the form for editting*/
function editTask(id) {
    fetch(`http://localhost:8080/tasks/${id}`)
        .then(response => response.json())
        .then(task => {
            editTaskId = id;

            document.querySelector("#title").value = task.title;
            document.querySelector("#description").value = task.description;
            document.querySelector("#status").value = task.status;
            document.querySelector("#employeeId").value = task.employee ? task.employee.id : "";

            document.getElementById("formTitle").textContent = "Update Task";
            document.getElementById("cancelEdit").style.display = "inline-block";

            window.scrollTo(0, 0);
        })
        .catch(err => console.log("Error", err));
}

//cancel editting
document.querySelector("#cancelEdit").addEventListener("click", function() {
    editTaskId = null;
    document.querySelector("#taskForm").reset();
    document.querySelector("#formTitle").textContent = "Create New Task";
    this.style.display = "none";
});


/*Change task status*/

function changeStatus(id, currentStatus) {
    const normalizedStatus = currentStatus
        .trim()
        .toLowerCase()
        .replace(/[\s-]+/g, "_");

    let status;

    if (normalizedStatus === "pending") {
        status = "in_progress";
    } else if (normalizedStatus === "in_progress") {
        status = "completed";
    } else if (normalizedStatus === "completed") {
        status = "under review"
    } else {
        showMessage("Error", "Unknown task status: " + currentStatus);
        return;
    }

    fetch(`http://localhost:8080/tasks/${id}/status`, {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ status: status })
    })
        .then(async response => {
            const responseText = await response.text();

            console.log("HTTP status:", response.status);
            console.log("Response body:", responseText);

            if (!response.ok) {
                throw new Error(responseText || "Could not update task status.");
            }

            loadTasks();
            showMessage("Success", "Task status updated successfully!");
        })
        .catch(error => {
            showMessage("Error", error.message);
        });
}

/*Delete task*/
let taskToDelete = null;
let isDeleting = false;

/*open confirmtion modal*/
function deleteTask(id) {
    taskToDelete = id;

    document.querySelector("#deleteConfirmation").style.display = "block";
    document.querySelector("#deleteSuccess").style.display = "none";
    document.querySelector("#deleteModal").style.display = "flex";


    //cancel deletion
    document.querySelector("#cancelDelete").addEventListener("click", function() {
        if (isDeleting) {
            return;
        }

        document.querySelector("#deleteModal").style.display = "none";
        taskToDelete = null;
    });


    //confirm deletion

    document.querySelector("#confirmDelete").addEventListener("click", function() {
        if (taskToDelete === null || isDeleting) {
            return;
        }

        isDeleting = true;
        const id = taskToDelete;
        fetch(`http://localhost:8080/tasks/${id}`, {
            method: "DELETE"
        })
            .then(async response => {
                const message = await response.text();

                console.log("Delete HTTP status:", response.status);
                console.log("Delete response:", message);

                if (!response.ok) {
                    throw new Error(message || "Could not delete task.");
                }

                return message;
            })
            .then(() => {
                document.querySelector("#deleteConfirmation").style.display = "none";
                document.querySelector("#deleteSuccess").style.display = "block";
                //taskToDelete = null;
                loadTasks();
            })
            .catch(error => {
                console.error(error);
                document.querySelector("#deleteModal").style.display = "none";
                taskToDelete = null;
                showMessage("Error", "Could not delete task. Please check the server.");
            })
            .finally(() => {
                isDeleting = false;
            });
    });

    //close success modal
    document.querySelector("#successOk").addEventListener("click", function() {
        document.querySelector("#deleteConfirmation").style.display = "block";
        document.querySelector("#deleteSuccess").style.display = "none";
        document.querySelector("#deleteModal").style.display = "none";
        taskToDelete = null;
    });

}
function showMessage(title, message) {
    document.querySelector("#messageTitle").textContent = title;
    document.querySelector("#messageText").textContent = message;
    document.querySelector("#messageModal").style.display = "flex";
};

// Close the message modal when OK is clicked
document.querySelector("#messageOk").addEventListener("click", function() {
    document.querySelector("#messageModal").style.display = "none";
});

/*Initial loading*/
loadEmployees();
loadTasks();


/*event listener for filtering*/
document.querySelector("#taskStatusFilter").addEventListener("change",function(){
	renderTasks();
})