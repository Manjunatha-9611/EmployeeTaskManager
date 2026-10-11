/*alert("NEW EMPLOYEES JS LOADED");
console.log("NEW employees.js loaded");*/
let editEmployeeId = null;
let allEmployees = [];

/*Get Employees fetch api*/
function loadEmployees(){
	fetch("/employees")
	.then(response=>{
			if(!response.ok){
				throw new Error("Failed to fecth employees: "+response.status);
			}
			return response.json()
		})
	.then(data=>{
		if(!Array.isArray(data)){
			throw new Error("Expected an array of employees from the API");
		}
		allEmployees = data;
	/*	console.log("Employees received:", allEmployees);
		console.log("Departments:", allEmployees.map(employee => employee.department));*/
		
		const departmentFilter = document.querySelector("#departmentFilter");
		const previousDepartment = departmentFilter.value;
		
		const departments = [...new Set(allEmployees.map(employee=> employee.department).filter(department => department))].sort();
		
		departmentFilter.innerHTML = `<option value="all">All Departments</option>`;
		
		departments.forEach(department =>{
			const option = document.createElement("option");
			option.value = department;
			option.textContent = department.charAt(0).toUpperCase()+department.slice(1);
			departmentFilter.appendChild(option);
		});
		if(departments.includes(previousDepartment)){
			departmentFilter.value = previousDepartment;
		}
		renderEmployees();
	}).catch(err=>console.log("Error :",err));
};

/*rendering data for filtering option*/
function renderEmployees(){
	const tableBody = document.querySelector("#employeeTableBody");
	const searchInput = document.querySelector("#employeeSearch");
	
	const departmentFilter = document.querySelector("#departmentFilter");
	
	if(!tableBody || !searchInput || !departmentFilter){
		console.error("Employee table or filter elements are missing from HTML");
	}
	
	const searchText = searchInput.value.trim().toLowerCase();
	const selectedDepartment = departmentFilter.value;
	
	const filteredEmployees = allEmployees.filter(employee=>{
		const name = String(employee.name||"").toLowerCase();
		const id = String(employee.id ?? "").toLowerCase();
		const email = String(employee.email || "").toLowerCase();
		const department = String(employee.department || "");
		
		const matchesSearch = name.includes(searchText) || id.includes(searchText) || email.includes(searchText);
		
		const matchesDepartment = selectedDepartment === "all" || department === selectedDepartment;
		
		return matchesSearch && matchesDepartment;
	});
	
	tableBody.innerHTML ="";
	
	if(filteredEmployees.length === 0){
		tableBody.innerHTML = `<tr><td colspan="5">No employees found</td></tr>`;
		return;
	}
	
	filteredEmployees.forEach(employee => {
		const row=document.createElement("tr");
	            row.innerHTML += `
				<td>${employee.name.charAt(0).toUpperCase() + employee.name.slice(1) || ""}</td>
				<td>${employee.email || ""}</td>
				<td>${employee.department.charAt(0).toUpperCase() + employee.department.slice(1) ||""}</td>
				<td>${employee.salary ?? ""}</td>
				<td>
					<button class="btn-edit" onclick="editEmployee(${employee.id})">Edit</button>
					<button class="btn-delete" onclick="openDeleteModal(${employee.id})">Delete</button>
	
			<tr>`;
			tableBody.appendChild(row);
	        });
};

/*adding employee*/
document.querySelector("#employeeForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const employee = {
        name: document.querySelector("#name").value,
        email: document.querySelector("#email").value,
        department: document.querySelector("#department").value,
        salary: document.querySelector("#salary").value,
    };
	
	let url = "/employees";
	let method = "POST";
	
	if(editEmployeeId !== null){
		url = `/employees/${editEmployeeId}`;
		method="PUT";
	}

    fetch(url, {
        method: method,
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(employee)
    })
        .then(response => response.json())
        .then(data => {
            console.log("Response", data);

            document.querySelector("#employeeForm").reset();
			if(editEmployeeId !== null){
				showMessage("Success","Employee updated successfully.");
			}else{
				showMessage("Success","Employee added successfully.");
			}
			editEmployeeId = null;
			loadEmployees();
        })
        .catch(err => console.log("Error :", err));
});


/*Deleting employee data*/

let employeeToDelete = null;

function openDeleteModal(id) {
    employeeToDelete = id;
    document.querySelector("#deleteModal").style.display = "flex";
};

/*yes delete section*/
document.querySelector("#confirmDelete").addEventListener("click", function() {
    fetch(`/employees/${employeeToDelete}`, {
        method: "DELETE"
    })
        .then(response => response.text())
        .then(data => {
            console.log(data);
            document.querySelector("#deleteConfirmation").style.display = "none";
            document.querySelector("#deleteSuccess").style.display = "block";
        })
        .catch(err => console.log("Error :", err));
});

/*reseting after clicking ok on success message*/
document.querySelector("#successOk").addEventListener("click", function() {
    document.querySelector("#deleteModal").style.display = "none";

    document.querySelector("#deleteSuccess").style.display = "none";

    document.querySelector("#deleteConfirmation").style.display = "block";
    employeeToDelete = null;
    location.reload();
});

/*also for no selection*/
document.querySelector("#cancelDelete").addEventListener("click", function() {

    document.getElementById("deleteModal").style.display = "none";

    employeeToDelete = null;

});



/*Editing the employee data*/
function editEmployee(id){
	
	editEmployeeId = id;
	
	fetch(`/employees/${id}`)
	.then(response => response.json())
	.then(employee => {
		document.querySelector("#name").value = employee.name;
		document.querySelector("#email").value = employee.email;
		document.querySelector("#department").value = employee.department.charAt(0).toUpperCase()+employee.department.slice(1);
		document.querySelector("#salary").value = employee.salary;
	})
	.catch(err=> console.log("Error :",err));
}

function showMessage(title,message){
	document.querySelector("#messageTitle").textContent = title;
	
	document.querySelector("#messageText").textContent = message;
	
	document.querySelector("#messageModal").style.display = "flex";
}

document.querySelector("#messageOk").addEventListener("click",function(){
	document.querySelector("#messageModal").style.display="none";
	location.reload();
});


/*Event Listener for employee search and filtering*/
document.querySelector("#employeeSearch").addEventListener("input",renderEmployees);
document.querySelector("#departmentFilter").addEventListener("change",renderEmployees);
loadEmployees();
