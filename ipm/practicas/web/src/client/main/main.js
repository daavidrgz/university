import {logged} from "../utils.js"

logged()

window.addEventListener('load', function () {
	setTimeout(() => document.getElementById("cover").classList.remove("show-cover"), 100)
})

document.getElementById("login").addEventListener("click", () => {
	document.getElementById("cover").classList.add("show-cover")
	setTimeout(() => window.location.href="../login/login.html", 500)
})
document.getElementById("register").addEventListener("click", () => {
	document.getElementById("cover").classList.add("show-cover")
	setTimeout(() => window.location.href="../register/register.html", 500)
})
