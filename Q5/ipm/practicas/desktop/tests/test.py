#! /usr/bin/python3

import pytest
import requests
import time
from datetime import datetime
from ipm import e2e
import gi

gi.require_version("Atspi", "2.0")
from gi.repository import Atspi

PATH="./ipm-p1.py"
HEADERS={"x-hasura-admin-secret":"myadminsecretkey"}
ENDPOINT="http://localhost:8080/api/rest"
NAME="testv11"
SURNAME="testv11"
DAY="01"
MONTH="07"
YEAR="2000"

@pytest.fixture
def app():
	process,app = e2e.run(PATH)
	if app is None:
		process and process.kill()
		assert False, f"There is no application {PATH}"
	yield app
	process and process.kill()

def generate_user(name,surname):
	r = requests.post(
		f"{ENDPOINT}/user",
		headers=HEADERS,
		data={"username":"test","password":"test","name":name,"surname":surname,
		"phone":"555555","email":"test@pytest","is_vaccinated":"true"}
	)
	uuid = r.json()["insert_users_one"]["uuid"]
	r = requests.get(f"{ENDPOINT}/facilities?limit=1",headers=HEADERS)
	facility_id = r.json()["facilities"][0]["id"]

	for i in range(4): # Insert 4 in-out accesses
		r = requests.post(
			f"{ENDPOINT}/access_log",
			headers=HEADERS,
			json={"facility_id":facility_id,"user_id":uuid,"timestamp":f"{YEAR}-{MONTH}-0{1+i}T18:00:00+00:00",
			"type":"IN","temperature":"35.7"}
		)
		r = requests.post(
			f"{ENDPOINT}/access_log",
			headers=HEADERS,
			json={"facility_id":facility_id,"user_id":uuid,"timestamp":f"{YEAR}-{MONTH}-0{1+i}T23:00:00+00:00",
			"type":"OUT","temperature":"35.7"}
		)
	# Insert 16 contacts
	for i in range(16):
		r = requests.post(
			f"{ENDPOINT}/user",
			headers=HEADERS,
			data={"username":f"{name}-contact{i}","password":"test","name":f"{name}-contact{i}",
			"surname":f"{surname}-contact{i}","phone":"555555","email":"test@pytest","is_vaccinated":"true"}
		)
		contact_uuid = r.json()["insert_users_one"]["uuid"]
		r = requests.post(
			f"{ENDPOINT}/access_log",
			headers=HEADERS,
			json={"facility_id":facility_id,"user_id":contact_uuid,"timestamp":f"{YEAR}-{MONTH}-{DAY}T19:00:00+00:00",
			"type":"IN","temperature":"35.7"}
		)
		r = requests.post(
			f"{ENDPOINT}/access_log",
			headers=HEADERS,
			json={"facility_id":facility_id,"user_id":contact_uuid,"timestamp":f"{YEAR}-{MONTH}-{DAY}T22:00:00+00:00",
			"type":"OUT","temperature":"35.7"}
		)
	return uuid

@pytest.fixture
def userdata():
	r = requests.get(f"{ENDPOINT}/user?name={NAME}&surname={SURNAME}", headers=HEADERS)
	users = r.json()["users"]
	
	if len(users) == 0:
		uuid = generate_user(NAME, SURNAME)
	else:
		uuid = users[0]["uuid"]
	
	# Get user data
	user_req = requests.get(f"{ENDPOINT}/users/{uuid}", headers=HEADERS)
	user = user_req.json()["users"][0]

	# Get first access
	access_req = requests.get(f"{ENDPOINT}/user_access_log/{uuid}?limit=1", headers=HEADERS)
	access = access_req.json()["access_log"][0]
	yield user,access
	# Should delete user after usage if there was an API's endpoint to delete users

def printTree(app):
	for obj in e2e.find_all_objs(app):
	
		print(f"""
		--------------------
		role:{obj.get_role()}
		name:{obj.get_name()}
		--------------------
		""")

def refresh(app):
	for obj in e2e.find_all_objs(app):
		obj.get_name()

def search(app, role="", name=""): # Bug with e2e module not refreshing widget's tree
	time.sleep(0.1)
	refresh(app)
	return e2e.find_obj(app, role=role, name=name)

def checkTable(app, name, strings, columns, min_rows=1):
	table = search(app, role="table", name=name)
	assert not isinstance(table, e2e.NotFoundError), f"No {name} table"

	assert table.get_n_columns() == columns
	assert table.get_n_rows() >= min_rows

	for i in range(columns):
		assert strings[table.get_column_description(i)] == \
			table.get_accessible_at(0,i).get_text(0,-1),f"""
			Failed on column: {table.get_column_description(i)},
			"""

def checkNavigation(app, name, max_rows, next_rows):
	do,shows = e2e.perform_on(app)
	table = search(app,role="table",name=name)
	assert not isinstance(table, e2e.NotFoundError), f"No {name} table"
	assert table.get_n_rows() == max_rows

	current_page = search(app, role="label", name="current_page")
	assert not isinstance(current_page, e2e.NotFoundError), f"No current_page label"
	assert current_page.get_text(0,-1) == "1"

	next_page = search(app, role="push button", name="next_page")
	assert not isinstance(next_page, e2e.NotFoundError), f"No next_page button"
	do("click", role="push button", name="next_page")

	current_page = search(app, role="label", name="current_page")
	assert not isinstance(current_page, e2e.NotFoundError), f"No current_page label"
	assert current_page.get_text(0,-1) == "2"

	new_table = search(app, role="table", name=name)
	assert not isinstance(new_table, e2e.NotFoundError), f"No {name} table"
	assert new_table.get_n_rows() == next_rows

	prev_page = search(app, role="push button", name="prev_page")
	assert not isinstance(prev_page, e2e.NotFoundError), f"No prev_page button"
	do("click", role="push button", name="prev_page")

	current_page = search(app, role="label", name="current_page")
	assert not isinstance(current_page, e2e.NotFoundError), f"No current_page label"
	assert current_page.get_text(0,-1) == "1"

def checkUserInfo(app,strings):
	for key,value in strings.items():
		obj = search(app, role="label", name=key)
		assert not isinstance(obj, e2e.NotFoundError), f"No {key} label"
		assert obj.get_text(0,-1) == f"{key}   •   {value}"

def test_search_info(app, userdata):
	user,access = userdata
	strings = {
		"UUID": user["uuid"],
		"Name": user["name"],
		"Surname": user["surname"],
		"Email": user["email"],
		"Phone": user["phone"],
	}
	date = datetime.fromisoformat(access["timestamp"])
	datestr = date.strftime("%H:%M  %d-%m-%Y")
	access_dict = {
		"Facility":access["facility"]["name"],
		"Date":datestr,
		"Entry/Exit":access["type"],
		"Temperature":f"{access['temperature']} °C"
	}

	entry = search(app, role="text",name="search_entry")
	assert not isinstance(entry, e2e.NotFoundError),"No search bar"
	
	entry.set_text_contents(f"{strings['Name']} {strings['Surname']}") # Set input text to user

	do,shows = e2e.perform_on(app)
	do('activate', role="text", name="search_entry")

	# Check for table values

	checkTable(app, "user_table", strings, columns=5, min_rows=1)
	table = search(app, role="table", name="user_table")
	table.add_row_selection(0)

	cell = search(app, role="table cell", name=strings["UUID"])
	assert not isinstance(cell, e2e.NotFoundError), "No clickable cell"
	do('activate', role='table cell', name=strings["UUID"])

	checkUserInfo(app, strings)
	
	checkTable(app, "access_table", access_dict, columns= 4, min_rows =1)
	checkNavigation(app, "access_table", max_rows=5, next_rows=3)


def test_contacts(app, userdata):
	user,access = userdata

	START_DATE_STR = f"{DAY}/{MONTH}/{YEAR}"
	strings = {
		"UUID": user["uuid"],
		"Name": user["name"],
		"Surname": user["surname"],
		"Email": user["email"],
		"Phone": user["phone"],
	}
	
	do,show = e2e.perform_on(app)

	contacts = search(app, role="radio button", name="Contacts")
	assert not isinstance(contacts, e2e.NotFoundError), "No contacts button"
	do("click", role="radio button", name="Contacts")

	entry = search(app, role="text", name="contacts_entry")
	assert not isinstance(entry, e2e.NotFoundError), "No search bar"

	entry.set_text_contents(f"{strings['Name']} {strings['Surname']}") # Set input text to user

	start = search(app, role="text", name="start_date_entry")
	assert not isinstance(start, e2e.NotFoundError), "No start date input"
	start.set_text_contents(START_DATE_STR)

	end = search(app, role="text", name="end_date_entry")
	assert not isinstance(end, e2e.NotFoundError), "No end date input"
	end.set_text_contents("") # Defaults -> Today date

	submit_button = search(app, role="push button", name="submit_button")
	assert not isinstance(submit_button, e2e.NotFoundError),"No submit button"
	do("click", role="push button", name="submit_button")
	
	# Check for table values
	checkTable(app, "user_table", strings, columns=5, min_rows=1)
	table = search(app, role="table", name="user_table")
	table.add_row_selection(0)

	cell = search(app, role="table cell", name=strings["UUID"])
	assert not isinstance(cell, e2e.NotFoundError), "No clickable cell"
	do('activate', role='table cell', name=strings["UUID"])
	time.sleep(3)
	# Don't check table, first row is undertermined	
	checkNavigation(app, "contacts_table", max_rows=10, next_rows=6)

def test_concurrency(app,userdata):
	# Eliminamos el timeout de arrancar la app
	Atspi.set_timeout(800, -1)
	
	user,access = userdata

	START_DATE_STR = f"{DAY}/{MONTH}/{YEAR}"
	strings = {
		"UUID": user["uuid"],
		"Name": user["name"],
		"Surname": user["surname"],
		"Email": user["email"],
		"Phone": user["phone"],
	}
	
	do,show = e2e.perform_on(app)

	contacts = search(app, role="radio button", name="Contacts")
	assert not isinstance(contacts, e2e.NotFoundError), "No contacts button"
	do("click", role="radio button", name="Contacts")

	entry = search(app, role="text", name="contacts_entry")
	assert not isinstance(entry, e2e.NotFoundError), "No search bar"

	entry.set_text_contents(f"{strings['Name']} {strings['Surname']}") # Set input text to user

	start = search(app, role="text", name="start_date_entry")
	assert not isinstance(start, e2e.NotFoundError), "No start date input"
	start.set_text_contents(START_DATE_STR)

	end = search(app, role="text", name="end_date_entry")
	assert not isinstance(end, e2e.NotFoundError), "No end date input"
	end.set_text_contents("") # Defaults -> Today date

	submit_button = search(app, role="push button", name="submit_button")
	assert not isinstance(submit_button, e2e.NotFoundError),"No submit button"
	do("click", role="push button", name="submit_button")
	
	# Check for table values
	checkTable(app, "user_table", strings, columns=5, min_rows=1)
	table = search(app, role="table", name="user_table")
	table.add_row_selection(0)

	cell = search(app, role="table cell", name=strings["UUID"])
	assert not isinstance(cell, e2e.NotFoundError), "No clickable cell"
	do('activate', role='table cell', name=strings["UUID"])

	assert app.get_name() != "", "La interface no responde"