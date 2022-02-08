import {login, logged, closeInfoBox} from "../utils.js"

const handleLogin = async (e)=>{
	e.preventDefault();

	closeInfoBox();

	const username = document.getElementById("username").value
	const password = document.getElementById("password").value

	await login(username,password)
}

//Si ya está logeado

document.getElementById("cover").classList.remove("show-cover")
document.getElementById("login-form").addEventListener("submit",handleLogin)
document.querySelector("#info-box").addEventListener("click", closeInfoBox)

document.getElementById("home").addEventListener("click", () => {
	document.getElementById("cover").classList.add("show-cover")
	setTimeout(() => window.location.href="../main/main.html", 500)
})

await logged()
