import {showError, closeInfoBox} from "../utils.js"
const endpoint = "http://ipm-go.hermo.me"

const getInfo = async () => {

	document.getElementById("qrcode").setAttribute("aria-busy", "true")
	document.querySelector(".info-container").setAttribute("aria-busy", "true")
	document.getElementById("access-list").setAttribute("aria-busy", "true")

	const username = sessionStorage.getItem("username");
	const password = sessionStorage.getItem("password");

	if ( !username || !password ) {
		window.location.href = "../main/main.html";
		return;
	}

	const response = await fetch(`${endpoint}/login`,{
		method: "POST",
		body: JSON.stringify({
			username,
			password
		})
	})
	if ( !response.ok ) {
		showError("Error while retrieving info");
		return;
	}

	const data = await response.json()

	if ( !data.users || data.users.length == 0 ) {
		showError("Incorrect username or password");
		return;
	}

	const info = data["users"][0]

	const qrapi = `${endpoint}/qr?name=${info.name}&surname=${info.surname}&uuid=${info.uuid}`
	document.getElementById("qrcode").src = qrapi
	document.getElementById("qrcode").setAttribute("aria-busy", "false")

	document.getElementById("name").innerText = `${info.name} ${info.surname}`
	document.getElementById("email").innerText = `${info.email}`
	document.getElementById("phone").innerText = `${info.phone}`
	document.getElementById("vaccinated").innerText = info.is_vaccinated ? "Yes" : "No"
	document.querySelector(".info-container").setAttribute("aria-busy", "false")

	const uuid = info.uuid
	const accessResponse = await fetch(`http://ipm-go.hermo.me/access?uuid=${uuid}`)
	const access = await accessResponse.json()

	const list = document.getElementById("access-list")

	access.map(item => {
		const entry = document.createElement("li")
		entry.classList.add("item")

		const facility = document.createElement("p")
		facility.innerText = item.facility

		const timestamp = document.createElement("p")
		const date = new Date(item.timestamp)
		timestamp.innerText = date.toUTCString().replace("GMT","")

		const temperature = document.createElement("p")
		temperature.innerText = item.temperature

		entry.appendChild(facility)
		entry.appendChild(timestamp)
		entry.appendChild(temperature)
		list.appendChild(entry)
	});

	document.getElementById("access-list").setAttribute("aria-busy", "false")
}
const handleLogOut = () => {
	sessionStorage.removeItem("username")
	sessionStorage.removeItem("password")

	document.getElementById("cover").classList.add("show-cover")
	setTimeout(() => window.location.href="../main/main.html", 500)
}

getInfo()
document.getElementById("cover").classList.remove("show-cover")
document.getElementById("logout").addEventListener("click", handleLogOut)
document.querySelector("#info-box").addEventListener("click", closeInfoBox)
