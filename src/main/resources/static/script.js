/*employees data fetch api - get*/
fetch("http://localhost:8080/employees")
.then(response=>response.json())
.then(data=>{
	
	
	/*total employees*/
	const totalEmployees = document.querySelector("#totalEmployees");
	totalEmployees.textContent = data.length;

	
	/*employee data table*/
	const tableBody = document.querySelector("#employeeTableBody");
	data.slice().reverse().slice(0,3).forEach(employee=>{
		tableBody.innerHTML +=`
		<tr>
			<td>${employee.name}</td>
			<td>${employee.department}</td>
		</tr>`;
	});
	
	/*employee statistics chart logic*/
			const departments = {};
			
			data.forEach(employee => {
				if(departments[employee.department]){
					departments[employee.department]++;
				}else{
					departments[employee.department] = 1;
				}
			});
			console.log(departments);
			
			const chart = document.querySelector("#employeeChart");
			for(let department in departments){
				const count = departments[department];
				
				chart.innerHTML +=`
				<div class="chart-row">
					<span>${department}</span>
					<div class="bar">
						<div class="bar-fill" style="width:${count*30}px;"></div>
					</div>
					<strong>${count}</strong>
				</div>`;
			}
})
.catch(error=>{
	console.log("Error:",error)
});



/*tasks data fetch api*/
fetch("http://localhost:8080/tasks")
.then(response=>response.json())
.then(data => {
	document.querySelector("#totalTasks").textContent=data.length;
})
.catch(err=>{
	console.log("Error :",err);
});


/*tasks data for completed*/
fetch("http://localhost:8080/tasks/status/completed")
.then(response=>response.json())
.then(data =>{
	document.querySelector("#completedTasks").textContent=data.length;
	document.querySelector("#completedCount").textContent=data.length;
})
.catch(err=> {console.log("Error :",err)
});


/*tasks data for pending*/
fetch("http://localhost:8080/tasks/status/pending")
.then(response => response.json())
.then(data => {
	document.querySelector("#pendingTasks").textContent = data.length;
	document.querySelector("#pendingCount").textContent = data.length;
})


/*tasks data for in-progress*/
fetch("http://localhost:8080/tasks/status/in_progress")
.then(response => response.json())
.then(data => {
	document.querySelector("#progressCount").textContent=data.length;
})
.catch(err=>console.log("Error :",err));