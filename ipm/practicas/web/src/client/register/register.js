import {login, logged, showError, showSuccess, closeInfoBox} from "../utils.js"
const endpoint = "http://ipm-go.hermo.me"


const getValues = (keys) =>{
	const dict={}
	keys.forEach(key => dict[key]=document.getElementById(key).value)
	return dict
}

const handleRegister = async (e) => {
	e.preventDefault();

	closeInfoBox();

	const username = document.getElementById("username").value;
	const password = document.getElementById("password").value;
	const personName = document.getElementById("name").value;

	const personSurname = document.getElementById("surname").value;
	const email = document.getElementById("email").value;
	const phone = document.getElementById("phone").value;

	const is_vaccinated = document.getElementById("is_vaccinated").checked.toString();
	const user = {
		username:username,
		password:password,
		name:personName,
		surname:personSurname,
		email:email,
		phone:phone,
		is_vaccinated:is_vaccinated
	}

	try {
		const register = await fetch(`${endpoint}/register`, {
			method: "POST",
			body: JSON.stringify(user)
		})

		if ( register.status == 400 ) {
			showError("User already exists");
			return;
		}

		if ( !register.ok ) {
			showError("Server error");
			return;
		}
	
		showSuccess("Registered successfully");
		
		// Sleep
		await new Promise(resolve => setTimeout(resolve, 1500));

	} catch {
		showError("Server error");
	}

	//Login
	await login(user.username,user.password)
}

document.getElementById("cover").classList.remove("show-cover")
document.getElementById("register-form").addEventListener("submit", handleRegister)
document.querySelector("#info-box").addEventListener("click", closeInfoBox)

document.getElementById("home").addEventListener("click", () => {
	document.getElementById("cover").classList.add("show-cover")
	setTimeout(() => window.location.href="../main/main.html", 500)
})

await logged()
