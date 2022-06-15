import requests
import datetime
import pyqrcode
import io

AUTH={"x-hasura-admin-secret":"myadminsecretkey"}
ENDPOINT="http://ipm.hermo.me/api/rest/"

class RequestCursor:
	def __init__(self, url, limit, params={}):
		self.url = url+"?"
		self.params=params
		self.limit = limit
		self.offset = 0
		self._currentSetVar = self._fetchSet()

	def currentSet(self):
		return self._currentSetVar

	def nextSet(self):
		if self.isLastPage(): #If its last page, dont increase offset
			return []
		self.offset+=self.limit
		self._currentSetVar = self._fetchSet()
		return self._currentSetVar

	def prevSet(self):
		if self.isFirstPage():#if its first page, dont decrease offset
			self.offset=0
			return []
		else: 
			self.offset-=self.limit
			self._currentSetVar = self._fetchSet()
			return self._currentSetVar

	def _fetchSet(self):
		r = requests.get(self.url+f"&offset={self.offset}&limit={self.limit}",
		headers=AUTH,json=self.params)
		data = r.json()
		return list(data.values())[0]

	def currentPage(self):
		return self.offset//self.limit + 1
	def isFirstPage(self):
		return self.currentPage() == 1
	def isLastPage(self):
		r = requests.get(self.url+f"&offset={self.offset+self.limit}&limit={self.limit}",
			headers=AUTH,json=self.params)
		data = r.json()
		return len(list(data.values())[0]) == 0

class AccessLogCursor(RequestCursor):#RequestCursor wrapper to AccessLogs
	def __init__(self ,uuid, limit, params={}):
		url = f"{ENDPOINT}/user_access_log/{uuid}"

		if "startdate" in params.keys() and "enddate" in params.keys():
			#Comprobar si queremos los accesos entre dos fechas
			url+="/daterange"
		super().__init__(url, limit, params)

	def currentSet(self):
		return self._generateAccess(super().currentSet())
	def nextSet(self):
		return self._generateAccess(super().nextSet())
	def prevSet(self):
		
		return self._generateAccess(super().prevSet())
	def _generateAccess(self, accesses):
		return [Access(item) for item in accesses]

class FullAccessCursor(AccessLogCursor):
	def __init__(self, uuid, facility_limit, start, end):
		params = {
			"startdate":start.__str__(),
			"enddate":end.__str__(),
		}
		super().__init__(uuid, facility_limit*2, params)

	def currentSet(self):
		return self._generateFullAccess(super().currentSet())
	def nextSet(self):
		return self._generateFullAccess(super().nextSet())
	def prevSet(self):
		return self._generateFullAccess(super().prevSet())

	def _generateFullAccess(self, accesses):
		return [FullAccess(in_access,out_access) for in_access,out_access
			in zip(accesses[::2], accesses[1::2])]

class CovidAlertCursor(RequestCursor):#RequestCursor wrapper to CovidAlerts
	def __init__(self, full_access, limit):
		url = f"{ENDPOINT}/facility_access_log/{full_access.facility.id}/daterange"
		#Añadir 1 segundo al timestamp para que no ponga el acceso de la persona que busco
		startdate = self._addSeconds(full_access.in_timestamp,1)
		enddate = self._addSeconds(full_access.out_timestamp,-1)
		#Quitar 1 segundo al timestamp para que no ponga el acceso de la persona que busco
		params = {
			"startdate":startdate.__str__(),
			"enddate":enddate.__str__()
		}
		super().__init__(url, limit, params)

	def currentSet(self):
		return self._generateAlert(super().currentSet())
	def nextSet(self):
		return self._generateAlert(super().nextSet())
	def prevSet(self):
		return self._generateAlert(super().prevSet())

	def _generateAlert(self, accesses):
		return [User(data['user']) for data in accesses]
	def _addSeconds(self, timestamp, seconds):
		date = datetime.datetime.fromisoformat(timestamp)
		delta = datetime.timedelta(0,seconds)
		newDate = date + delta
		return newDate

class FullAccess:
	def __init__(self, in_access, out_access):
		self.in_access = in_access
		self.out_access = out_access
		self.in_timestamp = self.in_access.timestamp
		self.out_timestamp = self.out_access.timestamp
		self.facility = self.in_access.facility

	def __repr__(self):
		facilitystr = self.facility.__str__().replace("\n","\n\t")
		str=f"Facility: {facilitystr}\n"
		str+=f"in_timestamp: {self.in_timestamp}\n"
		str+=f"out_timestamp: {self.out_timestamp}\n"
		return str

class Facility:
	def __init__(self, data):
		self.name = data['name']
		self.address = data['address']
		self.id = data['id']

	def __str__(self):
		str=""
		str+=f"\nname: {self.name}\n"
		str+=f"address: {self.address}\n"
		str+=f"id: {self.id}"
		return str

	def __repr__(self):
		return self.__str__()

class Access:
	def __init__(self, data):
		self.timestamp = data['timestamp']
		self.temperature = data['temperature']
		self.type = data['type']
		self.facility=Facility(data['facility'])

	def __str__(self):
		str=""
		str+=f"timestamp: {self.timestamp}\n"
		str+=f"temperature: {self.temperature}\n"
		str+=f"type: {self.type}\n"
		fac_str = self.facility.__str__().replace("\n","\n\t")
		str+=f"facility: {fac_str}\n"
		return str
	
	def __repr__(self):
		return self.__str__()

class User:
	def __init__(self, data):
		self.uuid = data['uuid']
		#self.username=data['username']
		self.name=data['name']
		self.surname=data['surname']
		self.email=data['email']
		self.phone=data['phone']
		self.is_vaccinated=data['is_vaccinated']
		self.access_cursor = None
		self.contact_cursor = None

	def __repr__(self):
		return f"{self.name} {self.surname}"

	def newAccessCursor(self, limit):
		self.access_cursor = AccessLogCursor(self.uuid,limit)
		return self.access_cursor
	def getAccessCursor(self):
		return self.access_cursor
	def newContactCursor(self, limit, start, end):
		self.contact_cursor = ContactCursor(self.uuid, limit, start,end)
		return self.contact_cursor
	def getContactCursor(self):
		return self.contact_cursor
	def generateQrcode(self):
		qr = pyqrcode.create(f"{self.name},{self.surname},{self.uuid}")
		buffer = io.BytesIO()
		qr.svg(buffer)
		return buffer.getvalue()

class Model:
	def newUserCursor(self, name, surname, limit):
		return UserCursor(name, surname, limit)

class ListCursor:
	def __init__(self, items, limit):
		self.items = items
		self.offset = 0
		self.limit = limit
		self.size = len(self.items)

	def currentSet(self):
		return self.items[self.offset:self.offset+self.limit]

	def nextSet(self):
		if self.isLastPage(): # Si estamos en la última página, no sumar al offset
			return []
		self.offset +=self.limit
		return self.currentSet()

	def prevSet(self):
		if self.isFirstPage(): # Si estamos en la primera página, no restar al offset
			return []
		self.offset-=self.limit
		return self.currentSet()

	def currentPage(self):
		return self.offset//self.limit + 1
	def isFirstPage(self):
		return self.currentPage() == 1
	def isLastPage(self):
		return self.offset + self.limit >= self.size

class UserCursor(ListCursor):
	def __init__(self, name, surname, limit):
		users = self._getUsers(name, surname)
		super().__init__(users, limit)

	def _getUsers(self, name, surname):
		url = f"{ENDPOINT}/user?name={name}&surname={surname}"
		r = requests.get(url,headers=AUTH)
		data = r.json()
		return [User(user_data) for user_data in data['users']]

class ContactCursor(ListCursor):
	def __init__(self, uuid, limit, start, end):
		self.start = start
		self.end = end
		alerts = self._getContacts(uuid)
		super().__init__(alerts, limit)

	def _getContacts(self, uuid):
		userSet=set()
		accesscursor = FullAccessCursor(uuid, 5, self.start, self.end)
		while True:
			for access in accesscursor.currentSet():
				covid = CovidAlertCursor(access, 5)
				while True:
					for user in covid.currentSet():
						if user.uuid != uuid : 
							userSet.add((access,user.uuid))
					if covid.isLastPage(): break
					covid.nextSet()
			# Simula un do-while, para que se ejecute la última página antes de salir
			if accesscursor.isLastPage(): break
			accesscursor.nextSet()
		alerts=[]
		for access,id in userSet:
			r = requests.get(f"{ENDPOINT}/users/{id}",headers=AUTH)
			data = r.json()
			alerts.append((access,User(data['users'][0])))
		alerts.sort(key=self._sortAlerts)
		return alerts

	def _sortAlerts(self,alert):
		access,_ = alert
		return access.facility.name
		
 
if __name__ == '__main__':
	#user = Model().getUserByName("Cristina","Vargas")
	#user = Model().getUserByName("Jose","Garcia")
	#cursor = AccessLogCursor(user.uuid,1)
	#print(cursor.currentSet())
	#print(user.access_cursor.currentSet())
	#print(user.access_cursor.nextSet())
	#print(Model().getCovidAlert(user.uuid ))
	#user.newAccessCursor(5)
	cursor = Model().newUserCursor("Cristina","Vargas",2)
	user = cursor.currentSet()[0]
	start = datetime.datetime(2000,12,12)
	end = datetime.datetime(2022,12,12)
	user.newContactCursor(50,start,end)
	selUser = user.getContactCursor().currentSet()[0]
	print(selUser[1])
	selUser[1].generateqrcode()
