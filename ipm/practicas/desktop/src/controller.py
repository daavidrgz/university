from datetime import datetime, timedelta

import threading, time

import gi
gi.require_version('Gtk', '3.0')
from gi.repository import GLib

class Controller:
	def __init__(self, view, model):
		self.view = view
		self.model = model
		self.view.set_callbacks(self)

		self.cursors = {
			"search-window": None,
			"contacts-window": None
		}

		self.runningThreads = {
			"search-window": False,
			"contacts-window": False
		}

		self.sDateStr = ""
		self.eDateStr = ""
	
	# UTILS ••

	def _parseUserInfo(self, user):
		userInfo = {
			"UUID": user.uuid,
			"Name": user.name,
			"Surname": user.surname,
			"Email": user.email,
			"Phone": user.phone,
			"Vaccinated": user.is_vaccinated,
			"qrcode": user.generateQrcode()
		}
		return userInfo

	def _parseUsersList(self, users):
		return [[user.uuid, user.name, user.surname, user.phone, user.email] for user in users]

	def _parseAccessesList(self, accesses):
		return [[elem.facility.name, self._getDate(elem.timestamp), elem.type, elem.temperature + " °C"] for elem in accesses]

	def _parseContactsList(self, contacts):
		return [[user.name, user.surname, access.facility.name, 
			self._getDate(access.in_timestamp), self._getDate(access.out_timestamp), user.phone, user.uuid] for access,user in contacts]

	def _getPageInfo(self, cursor):
		pageInfo = {
			"isFirst": cursor.isFirstPage(),
			"isLast": cursor.isLastPage(),
			"page": cursor.currentPage(),
		}
		return pageInfo

	def _getDate(self, timestampIso):
		date = datetime.fromisoformat(timestampIso)
		return date.strftime("%H:%M  %d-%m-%Y")

	def _formatDate(self, datetime):
		return datetime.strftime("%d/%m/%Y")


	# VIEW - MODEL ••
	def show_about_popup(self, *_):
		self.view.show_about_popup()

	def search_changed(self, widget, labelRevealer):
		show = widget.get_text() == ""
		self.view.toggle_label(show, labelRevealer)

	def start_date_changed(self, widget):
		self.sDateStr = widget.get_text()
		self.check_date(widget)

	def end_date_changed(self, widget):
		self.eDateStr = widget.get_text()
		self.check_date(widget)

	def check_date(self, widget):
		date = widget.get_text()
		if date == "":
			widget.get_style_context().remove_class('error')
			return
		format = "%d/%m/%Y"
		try:
			datetime.strptime(date, format)
			widget.get_style_context().remove_class('error')			
		except ValueError:
			widget.get_style_context().add_class('error')

	def startThread(self, windowstr, lambdaFun):
		if self.runningThreads[windowstr]:
			return
		self.runningThreads[windowstr] = True
		self.view.show_spinner(True)
		threading.Thread(target=lambdaFun, daemon=True).start()

	def endThread(self, windowstr, lambdaFun):
		self.runningThreads[windowstr] = False

		remaining = list(self.runningThreads.values()).count(True) > 0
		if not remaining:
			self.view.show_spinner(False)

		if lambdaFun:
			GLib.idle_add(lambdaFun)

	## Search Users ••

	def search_users(self, _, entryWidget, tableClickCb, windowstr):
		self.startThread(windowstr, lambda: self._thread_search_users(entryWidget, tableClickCb, windowstr))
	def _thread_search_users(self, entryWidget, tableClickCb, windowstr):
		try:
			completeName = entryWidget.get_text() # Solve change-event delay while writing the name
			if completeName == "":
				self.endThread(windowstr, None)
				return

			if len(completeName.split()) != 2:
				self.endThread(windowstr, lambda: self.view.set_no_user_found(completeName, windowstr))
				return

			[name, surname] = completeName.split()

			cursor = self.model.newUserCursor(name, surname, limit=5)
			self.cursors[windowstr] = cursor
			if len(cursor.currentSet()) == 0:
				self.endThread(windowstr, lambda: self.view.set_no_user_found(completeName, windowstr))
				return

			users = self._parseUsersList(cursor.currentSet())
			pageInfo = self._getPageInfo(cursor)

			self.endThread(windowstr, lambda: self.view.set_users_list(self, users, pageInfo, tableClickCb, windowstr))
			
		except:
			self.endThread(windowstr, lambda: self.view.show_internal_error_popup())

	def next_user_page(self, _, windowstr):
		self.startThread(windowstr, lambda: self._thread_next_user_page(windowstr))
	def _thread_next_user_page(self, windowstr):
		try:
			cursor = self.cursors[windowstr]
			users = self._parseUsersList(cursor.nextSet())
			pageInfo = self._getPageInfo(cursor)
			self.endThread(windowstr, lambda: self.view.update_users_list(self, users, pageInfo, windowstr))
		except:
			self.endThread(windowstr, lambda: self.view.show_internal_error_popup())

	def prev_user_page(self, _, windowstr):
		self.startThread(windowstr, lambda: self._thread_prev_user_page(windowstr))
	def _thread_prev_user_page(self, windowstr):
		try:
			cursor = self.cursors[windowstr]
			users = self._parseUsersList(cursor.prevSet())
			pageInfo = self._getPageInfo(cursor)
			self.endThread(windowstr, lambda: self.view.update_users_list(self, users, pageInfo, windowstr))
		except:
			self.endThread(windowstr, lambda: self.view.show_internal_error_popup())

	## Search Accesses ••

	def search_accesses(self, widget, *_):
		self.startThread("search-window", lambda: self._thread_search_accesses(widget))
	def _thread_search_accesses(self, widget):
		try:
			model, treeiter = (widget.get_selection().get_selected())
			userUUID = model[treeiter][0]

			users = self.cursors["search-window"].currentSet()
			filteredUsers = filter(lambda user: user.uuid == userUUID, users)
			self.currentUser = list(filteredUsers)[0]
			
			# Initialize the access cursor
			self.accessCursor = self.currentUser.newAccessCursor(limit=5)
			accesses = self._parseAccessesList(self.accessCursor.currentSet())
			pageInfo = self._getPageInfo(self.accessCursor)

			self.endThread("search-window", lambda: 
				self.view.set_user_info(self._parseUserInfo(self.currentUser), self, accesses, pageInfo))
		except:
			self.endThread("search-window", lambda: self.view.show_internal_error_popup())
	
	def next_access_page(self, *_):
		self.startThread("search-window", lambda: self._thread_next_access_page())
	def _thread_next_access_page(self):
		try:
			accesses = self._parseAccessesList(self.accessCursor.nextSet())
			pageInfo = self._getPageInfo(self.accessCursor)
			self.endThread("search-window", lambda: self.view.update_user_accesses(self, accesses, pageInfo))
		except:
			self.endThread("search-window", lambda: self.view.show_internal_error_popup())

	def prev_access_page(self, *_):
		self.startThread("search-window", lambda: self._thread_prev_access_page())
	def _thread_prev_access_page(self):
		try:
			accesses = self._parseAccessesList(self.accessCursor.prevSet())
			pageInfo = self._getPageInfo(self.accessCursor)
			self.endThread("search-window", lambda: self.view.update_user_accesses(self, accesses, pageInfo))
		except:
			self.endThread("search-window", lambda: self.view.show_internal_error_popup())
	
	## Search Contacts ••

	def search_contacts(self, widget, *_):
		self.startThread("contacts-window", lambda: self._thread_search_contacts(widget))
	def _thread_search_contacts(self, widget):
		try:
			model, treeiter = (widget.get_selection().get_selected())
			userUUID = model[treeiter][0]

			users = self.cursors["contacts-window"].currentSet()
			filteredUsers = filter(lambda user: user.uuid == userUUID, users)
			self.currentUser = list(filteredUsers)[0]

			try:
				format = "%d/%m/%Y"
				if self.eDateStr == "":
					eTime = datetime.now()
				else:
					eTime = datetime.strptime(self.eDateStr, format)
				if self.sDateStr == "":
					sTime = eTime - timedelta(days=14)
				else:
					sTime = datetime.strptime(self.sDateStr, format)

				if sTime >= eTime: raise ValueError("Start date cannot be after end date")
			except ValueError:
				self.endThread("contacts-window", lambda: self.view.show_invalid_date_popup())
				return

			self.contactCursor = self.currentUser.newContactCursor(10, sTime, eTime)
			contacts = self._parseContactsList(self.contactCursor.currentSet())
			pageInfo = self._getPageInfo(self.contactCursor)

			self.endThread("contacts-window", lambda: 
				self.view.set_user_contacts(self, contacts, pageInfo, self._formatDate(sTime), self._formatDate(eTime))) 

		except:
			self.endThread("contacts-window", lambda: self.view.show_internal_error_popup())

	def next_contact_page(self, *_):
		self.startThread("contacts-window", lambda: self._thread_next_contact_page())
	def _thread_next_contact_page(self):
		try:
			contacts = self._parseContactsList(self.contactCursor.nextSet())
			pageInfo = self._getPageInfo(self.contactCursor)
			self.endThread("contacts-window", lambda: self.view.update_user_contacts(self, contacts, pageInfo))
		except:
			self.endThread("contacts-window", lambda: self.view.show_internal_error_popup())
	
	def prev_contact_page(self, *_):
		self.startThread("contacts-window", lambda: self._thread_prev_contact_page())
	def _thread_prev_contact_page(self):
		try:
			contacts = self._parseContactsList(self.contactCursor.prevSet())
			pageInfo = self._getPageInfo(self.contactCursor)
			self.endThread("contacts-window", lambda: self.view.update_user_contacts(self, contacts, pageInfo))
		except:
			self.endThread("contacts-window", lambda: self.view.show_internal_error_popup())
