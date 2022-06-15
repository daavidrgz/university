import express from "express";
import cors from "cors";
import fetch from "node-fetch"

const app = express();
const endpoint = "http://localhost:8080"

app.use(cors())
app.use(express.urlencoded({extended:true}))

const port = 3004

app.use(express.static("../client"))

app.get("/",(req,res)=>{

	res.redirect("/main/main.html")
})
app.get("/access",async (req,res)=>{
	const uuid = req.query.uuid

	const response = await fetch(`${endpoint}/api/rest/user_access_log/${uuid}`,{
		headers: {
			"x-hasura-admin-secret": "myadminsecretkey"
		}
	})
	const data = await response.json()

	const access = data.access_log
	access.sort((a,b)=>{
		const dateA = new Date(a.timestamp)
		const dateB = new Date(b.timestamp)
		return  (dateA < dateB) - (dateA > dateB)
	})
	const filtered = access.filter(item => item.type =="IN")
	const desiredAccess = filtered.slice(0,11)
	console.log(access)
	res.send(JSON.stringify(desiredAccess))
})

app.listen(port,()=> console.log("server started"))