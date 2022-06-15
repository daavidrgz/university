import gi

gi.require_version("Gtk", "3.0")
from gi.repository import Gtk, Atk, GdkPixbuf

class TableTopBarWidget:
	def __init__(self, title, prevCb, nextCb, pageInfo, windowstr, subtitle=""):
		self.tableButtons = Gtk.Box(spacing=10)

		titleBox = Gtk.Box(spacing=12)

		titleLabel = Gtk.Label()
		titleLabel.set_markup(f'<span size="large"><b>{title}</b></span>')
		titleBox.add(titleLabel)
		
		titlePixbuf = GdkPixbuf.Pixbuf.new_from_file_at_scale(f'assets/table.svg', width=16, height=16, preserve_aspect_ratio=True)
		titleIcon = Gtk.Image.new_from_pixbuf(titlePixbuf)
		titleBox.add(titleIcon)
		self.tableButtons.pack_start(titleBox, False, False, 0)
		
		sepBox = Gtk.Box(orientation=Gtk.Orientation.VERTICAL)
		sep = Gtk.HSeparator()
		sepBox.set_center_widget(sep)
		self.tableButtons.pack_start(sepBox, True, True, 20)

		arrowNext = Gtk.Button.new_from_icon_name("go-next", size=1)
		arrowNext.connect("clicked", nextCb, windowstr)
		arrowNext.get_accessible().set_name("next_page") # ATSPI
		if pageInfo["isLast"]: arrowNext.set_sensitive(False)
		self.tableButtons.pack_end(arrowNext, False, False, 5)

		pageLabel = Gtk.Label()
		pageLabel.set_markup(f'<span size="large"><b>{pageInfo["page"]}</b></span>')
		pageLabel.get_accessible().set_name("current_page") # ATSPI

		self.tableButtons.pack_end(pageLabel, False, False, 5)

		arrowPrev = Gtk.Button.new_from_icon_name("go-previous", size=1)
		arrowPrev.connect("clicked", prevCb, windowstr)
		arrowPrev.get_accessible().set_name("prev_page") # ATSPI

		if pageInfo["isFirst"]: arrowPrev.set_sensitive(False)
		self.tableButtons.pack_end(arrowPrev, False, False, 5)

		subtitleLabel = Gtk.Label()
		subtitleLabel.set_markup(f'<span size="medium"><b>{subtitle}</b></span>')
		self.tableButtons.pack_end(subtitleLabel, False, False, 20)
	
	def get_widget(self):
		return self.tableButtons

class TableWidget:
	def __init__(self, tableData, headers, tableClickCb=None):
		listStore = Gtk.ListStore()
		listStore.set_column_types([type(e) for e in headers])
		for row in tableData:
			listStore.append(row)

		table = Gtk.TreeView(model=listStore, activate_on_single_click=False)
		table.get_accessible().set_name("current_table")
		#print(table.get_accessible().get_role())
		for i, columnTitle in enumerate(headers):
			renderer = Gtk.CellRendererText()
			renderer.set_padding(20, 5)
			column = Gtk.TreeViewColumn(columnTitle, renderer, text=i)
			table.append_column(column)

		table.set_grid_lines(Gtk.TreeViewGridLines.BOTH)
		if tableClickCb:
			table.connect('row-activated', tableClickCb)

		self.scrollTable = Gtk.ScrolledWindow()
		self.scrollTable.set_policy(Gtk.PolicyType.AUTOMATIC, Gtk.PolicyType.NEVER)
		self.scrollTable.add(table)
	
	def get_widget(self):
		return self.scrollTable

class UserNotFoundWidget:
	def __init__(self, name):
		self.noUserFoundRevealer = Gtk.Revealer(reveal_child=False, transition_type=Gtk.RevealerTransitionType.CROSSFADE, transition_duration=200)
		noUserFoundLabel = Gtk.Label()
		noUserFoundLabel.set_markup(f'<span size="x-large">No user found with name <b>{name}</b>!</span>')
		self.noUserFoundRevealer.add(noUserFoundLabel)
	
	def get_widget(self):
		return self.noUserFoundRevealer

class UserListWidget:
	def __init__(self, tableClickCb, windowstr):
		self.tableClickCb = tableClickCb
		self.windowstr = windowstr
		self.userTableExternalContainer = Gtk.Box(orientation=Gtk.Orientation.VERTICAL, spacing=100)
		self.userTableContainer = None

	def set_users_list(self, ctr, userList, pageInfo):
		if self.userTableContainer:
			self.userTableExternalContainer.remove(self.userTableContainer)
		self.userTableContainer = Gtk.Box(orientation = Gtk.Orientation.VERTICAL, spacing=10)

		headers = ["UUID", "Name", "Surname", "Phone", "Email"]
		userTable = TableWidget(userList, headers, self.tableClickCb).get_widget()
		userTable.get_child().get_accessible().set_name("user_table")

		userTableRevealer = Gtk.Revealer(reveal_child=True, transition_type=Gtk.RevealerTransitionType.CROSSFADE, transition_duration=300)
		userTableRevealer.add(userTable)


		userTableButtons = TableTopBarWidget("Users", ctr.prev_user_page, ctr.next_user_page, pageInfo, self.windowstr).get_widget()

		self.userTableContainer.pack_start(userTableButtons, False, False, 20)
		self.userTableContainer.pack_start(userTableRevealer, False, False, 0)
		self.userTableExternalContainer.pack_start(self.userTableContainer, False, False, 0)

	def get_widget(self):
		return self.userTableExternalContainer

class UserInfoWidget:
	def __init__(self, userInfo):
		self.userInfoExternalContainer = Gtk.Box(orientation=Gtk.Orientation.VERTICAL, spacing=30)
		self.accessTableContainer = None

		userInfoContainer = Gtk.Box(spacing=10)
		self.userInfoExternalContainer.pack_start(userInfoContainer, False, False, 0)

		# Qrcode buffer to GdkPixbuf
		buffer = userInfo['qrcode']
		loader = GdkPixbuf.PixbufLoader.new_with_type("svg")
		loader.set_size(300,300)
		loader.write(buffer)
		loader.close()
		pixbuf = loader.get_pixbuf()

		qrCode = Gtk.Image.new_from_pixbuf(pixbuf)
		userInfoContainer.pack_start(qrCode, False, False, 0)

		userInfoText = Gtk.Box(orientation=Gtk.Orientation.VERTICAL, spacing=10, margin_top=20)
		userInfoTextScroll = Gtk.ScrolledWindow()
		userInfoTextScroll.set_policy(Gtk.PolicyType.AUTOMATIC, Gtk.PolicyType.NEVER)
		userInfoTextScroll.add(userInfoText)

		userInfoContainer.pack_start(userInfoTextScroll, True, True, 0)
		for key,value in userInfo.items():
			if key == "qrcode": continue
			
			labelBox = Gtk.Box(spacing=12)

			iconPixbuf = GdkPixbuf.Pixbuf.new_from_file_at_scale(f'assets/{key}.svg', width=15, height=15, preserve_aspect_ratio=True)
			icon = Gtk.Image.new_from_pixbuf(iconPixbuf)
			labelBox.add(icon)

			label = Gtk.Label()
			label.set_markup(f'<span size="large"><b>{key}</b>   •   {value}</span>')
			label.set_xalign(0)
			#ATSPI
			label.get_accessible().set_name(key)

			labelBox.add(label)

			userInfoText.pack_start(labelBox, False, False, 0)

	def set_accesses_list(self, ctr, accessesList, pageInfo): # Different function to update only the table insted of the entire widget
		# Remove the previous table and reset the widget
		if self.accessTableContainer:
			self.userInfoExternalContainer.remove(self.accessTableContainer)
		self.accessTableContainer = Gtk.Box(orientation = Gtk.Orientation.VERTICAL, spacing=10)

		accessTableButtons = TableTopBarWidget("Accesses Log", ctr.prev_access_page, ctr.next_access_page, pageInfo, "search-window").get_widget()

		headers = ["Facility", "Date", "Entry/Exit", "Temperature"]
		accessTable = TableWidget(accessesList, headers).get_widget()
		accessTable.get_child().get_accessible().set_name("access_table")
		# Revealer to delay the table representation and avoid visual bugs
		accessTableRevealer = Gtk.Revealer(reveal_child=True, transition_type=Gtk.RevealerTransitionType.CROSSFADE, transition_duration=0)
		accessTableRevealer.add(accessTable)

		self.accessTableContainer.pack_start(accessTableButtons, False, False, 20)
		self.accessTableContainer.pack_start(accessTableRevealer, False, False, 0)
		self.userInfoExternalContainer.pack_start(self.accessTableContainer, False, False, 0)

	def get_widget(self):
		return self.userInfoExternalContainer

class ContactListWidget:
	def __init__(self, sDate, eDate):
		self.sDate = sDate
		self.eDate = eDate
		self.contactsTableExternalContainer = Gtk.Box(orientation=Gtk.Orientation.VERTICAL, spacing=100)
		self.contactsTableContainer = None

	def set_contacts_list(self, ctr, contactList, pageInfo):
		if self.contactsTableContainer:
			self.contactsTableExternalContainer.remove(self.contactsTableContainer)
		self.contactsTableContainer = Gtk.Box(orientation=Gtk.Orientation.VERTICAL, spacing=10)

		contactTableButton = TableTopBarWidget("Contacts Log", ctr.prev_contact_page, ctr.next_contact_page,
			pageInfo, "contacts-window", subtitle=f"  ({self.sDate} - {self.eDate}) ").get_widget()

		headers = ["Name", "Surname", "Facility", "Date in", "Date out", "Phone", "UUID"]
		contactsTable = TableWidget(contactList, headers).get_widget()
		contactsTable.get_child().get_accessible().set_name("contacts_table")
		
		# Revealer to delay the table representation and avoid visual bugs
		contactsTableRevealer = Gtk.Revealer(reveal_child=True, transition_type=Gtk.RevealerTransitionType.CROSSFADE, transition_duration=0)
		contactsTableRevealer.add(contactsTable)

		self.contactsTableContainer.pack_start(contactTableButton, False, False, 20)
		self.contactsTableContainer.pack_start(contactsTableRevealer, False, False, 0)
		self.contactsTableExternalContainer.pack_start(self.contactsTableContainer, False, False, 0)
	
	def get_widget(self):
		return self.contactsTableExternalContainer		

class View:
	def __init__(self):
		self.w = Gtk.Window(title="Covid Radar")
		self.w.connect('delete-event', Gtk.main_quit)
		self.w.set_border_width(40)

		# MAIN BOX ••
		windowWraper =  Gtk.Box(orientation=Gtk.Orientation.VERTICAL, spacing=40)
		self.w.add(windowWraper)

		# TOP BAR ••
		topBar = Gtk.Box(spacing=20)
		logoPixbuf = GdkPixbuf.Pixbuf.new_from_file_at_scale(filename='assets/covid2.png', width=50, height=50, preserve_aspect_ratio=True)
		appLogo = Gtk.Image.new_from_pixbuf(logoPixbuf)
		topBar.add(appLogo)

		appTitle = Gtk.Label()
		appTitle.set_markup('<span size="xx-large"><b>Covid Radar</b></span>')
		topBar.add(appTitle)

		aboutPixbuf = GdkPixbuf.Pixbuf.new_from_file_at_scale(filename='assets/info.svg', width=15, height=15, preserve_aspect_ratio=True)
		aboutImage = Gtk.Image.new_from_pixbuf(aboutPixbuf)
		self.aboutButton = Gtk.Button()
		self.aboutButton.set_image(aboutImage)
		aboutBox = Gtk.Box(orientation=Gtk.Orientation.VERTICAL)
		aboutBox.pack_start(self.aboutButton, False, False, 0)
		topBar.pack_end(aboutBox, False, False, 0)

		self.spinner = Gtk.Spinner()
		topBar.pack_start(self.spinner, False, False, 10)
		
		windowWraper.pack_start(topBar, False, False, 0)

		# WINDOWS STACK ••
		stack = Gtk.Stack(transition_type=6, transition_duration=175)
		stackSwitcher = Gtk.StackSwitcher(stack=stack)
		stackSwitcherCenter = Gtk.Box()
		stackSwitcherCenter.set_center_widget(stackSwitcher)

		windowWraper.pack_start(stackSwitcherCenter, False, False, 0)
		windowWraper.pack_start(stack, True, True, 0)

		## SEARCH ••
		self.searchBox = Gtk.Box(orientation=Gtk.Orientation.VERTICAL, spacing=30, margin_left=50, margin_right=50)
		stack.add_titled(self.searchBox, "search-window", "Search")

		self.searchLabelRevealer = Gtk.Revealer(reveal_child=True, transition_type=Gtk.RevealerTransitionType.CROSSFADE, transition_duration=150)
		searchLabel = Gtk.Label()
		searchLabel.set_markup('<span size="medium"><i>Search for covid information about a person</i></span>')
		self.searchLabelRevealer.add(searchLabel)
		self.searchBox.pack_start(self.searchLabelRevealer, False, False, 0)

		self.searchEntry = Gtk.SearchEntry(placeholder_text="Name...")
		self.searchEntry.get_accessible().set_name("search_entry")

		self.searchBox.pack_start(self.searchEntry, False, False, 0)

		## CONTACTS ••
		self.contactsBox = Gtk.Box(orientation=Gtk.Orientation.VERTICAL, spacing=30, margin_left=50, margin_right=50)
		stack.add_titled(self.contactsBox, "contacts-window", "Contacts")

		self.contactsLabelRevealer = Gtk.Revealer(reveal_child=True, transition_type=Gtk.RevealerTransitionType.CROSSFADE, transition_duration=150)
		contactsLabel = Gtk.Label()
		contactsLabel.set_markup('<span size="medium"><i>Search for a person\'s contacts between two dates</i></span>')
		self.contactsLabelRevealer.add(contactsLabel)
		self.contactsBox.pack_start(self.contactsLabelRevealer, False, False, 0)

		self.contactSearchEntry = Gtk.SearchEntry(placeholder_text="Name...")
		self.contactSearchEntry.get_accessible().set_name("contacts_entry") #ATSPI


		self.contactsBox.pack_start(self.contactSearchEntry, False, False, 0)

		## Date Entries ••

		datesBox = Gtk.Box(orientation=Gtk.Orientation.HORIZONTAL, spacing=50)
		startDateLabel = Gtk.Label(label= "Start date")
		self.startDateEntry = Gtk.Entry(placeholder_text="DD/MM/YYYY")
		self.startDateEntry.get_accessible().add_relationship(Atk.RelationType.LABELLED_BY,
															startDateLabel.get_accessible())

		self.startDateEntry.get_accessible().set_name("start_date_entry") #ATSPI

		startDateBox = Gtk.Box(orientation=Gtk.Orientation.VERTICAL)
		startDateBox.pack_start(startDateLabel, True, True, 0)
		startDateBox.pack_start(self.startDateEntry, False, False, 10)
		
		endDateLabel = Gtk.Label(label= "End date")
		self.endDateEntry = Gtk.Entry(placeholder_text="DD/MM/YYYY")
		self.endDateEntry.get_accessible().add_relationship(Atk.RelationType.LABELLED_BY,
															endDateLabel.get_accessible())
		self.endDateEntry.get_accessible().set_name("end_date_entry") #ATSPI

		endDateBox = Gtk.Box(orientation=Gtk.Orientation.VERTICAL)
		endDateBox.pack_start(endDateLabel, True, True, 0)
		endDateBox.pack_start(self.endDateEntry, False, False, 10)

		datesBox.pack_start(startDateBox, False, False, 0)
		datesBox.pack_start(endDateBox, False, False, 0)

		centerDatesBox = Gtk.Box()
		centerDatesBox.set_center_widget(datesBox)

		self.contactsBox.pack_start(centerDatesBox, False, False, 0)

		## Sumbit Button ••

		self.submitButton = Gtk.Button.new_with_label("")
		self.submitButton.get_child().set_markup('<span size="medium">  Search  </span>')
		self.submitButton.get_accessible().set_name("submit_button") #ATSPI

		submitButtonCenterBox = Gtk.Box(orientation=Gtk.Orientation.HORIZONTAL, spacing=10)
		submitButtonCenterBox.pack_start(self.submitButton, True, False, 0)

		self.contactsBox.pack_start(submitButtonCenterBox, False, False, 0)

		# OTHER ••

		self.windows = {
			"search-window": {
				"main": self.searchBox,
				"user-table": None,
				"user-info": None,
				"user-not-found": None
			},
			"contacts-window": {
				"main": self.contactsBox,
				"user-table": None,
				"user-contacts": None,
				"user-not-found": None
			}
		}

		self.w.show_all()

	#  MAIN WIDGETS ••

	## Users Table ••

	def set_users_list(self, ctr, userList, pageInfo, tableClickCb, windowstr):
		self._clear_screen(windowstr)

		userListWidget = UserListWidget(tableClickCb, windowstr)
		userListWidget.set_users_list(ctr, userList, pageInfo)

		self.windows[windowstr]["user-table"] = userListWidget
		self.windows[windowstr]["main"].pack_start(userListWidget.get_widget(), True, True, 0)

		self.w.show_all()
	def update_users_list(self, ctr, userList, pageInfo, windowstr):
		self.windows[windowstr]["user-table"].set_users_list(ctr, userList, pageInfo)
		self.w.show_all()

	## User Info and Accesses Table ••
	def set_user_info(self, userInfo, ctr, accesses, pageInfo):
		self._clear_screen('search-window')

		userWidget = UserInfoWidget(userInfo)
		userWidget.set_accesses_list(ctr, accesses, pageInfo)

		self.windows["search-window"]["user-info"] = userWidget
		self.searchBox.pack_start(userWidget.get_widget(), True, True, 10)

		self.w.show_all()
	def update_user_accesses(self, ctr, accesses, pageInfo):
		self.windows["search-window"]["user-info"].set_accesses_list(ctr, accesses, pageInfo)
		self.w.show_all()
		
	## Contacts Table ••

	def set_user_contacts(self, ctr, contacts, pageInfo, sDate, eDate):
		self._clear_screen('contacts-window')

		contactListWidget = ContactListWidget(sDate, eDate)
		contactListWidget.set_contacts_list(ctr, contacts, pageInfo)

		self.windows["contacts-window"]["user-contacts"] = contactListWidget
		self.contactsBox.pack_start(contactListWidget.get_widget(), True, True, 0)
		
		self.w.show_all()
	def update_user_contacts(self, ctr, contacts, pageInfo):
			self.windows["contacts-window"]["user-contacts"].set_contacts_list(ctr, contacts, pageInfo)
			self.w.show_all()


	# UTILS ••
	def show_spinner(self, show):
		self.spinner.start() if show else self.spinner.stop()

	def _clear_screen(self, windowstr):
		currentWindow = self.windows[windowstr]["main"]

		if self.windows["search-window"]["user-info"] and windowstr == "search-window":
			currentWindow.remove(self.windows["search-window"]["user-info"].get_widget())

		if self.windows["contacts-window"]["user-contacts"] and windowstr == "contacts-window":
			currentWindow.remove(self.windows["contacts-window"]["user-contacts"].get_widget())

		if self.windows[windowstr]["user-table"]:
			currentWindow.remove(self.windows[windowstr]["user-table"].get_widget())

		if self.windows[windowstr]["user-not-found"]:
			currentWindow.remove(self.windows[windowstr]["user-not-found"].get_widget())

	def set_no_user_found(self, name, windowstr):
		self._clear_screen(windowstr)

		userNotFoundWidget = UserNotFoundWidget(name)

		self.windows[windowstr]["user-not-found"] = userNotFoundWidget
		self.windows[windowstr]["main"].pack_start(userNotFoundWidget.get_widget(), False, False, 0)

		self.w.show_all()
		userNotFoundWidget.get_widget().set_reveal_child(True)

	def toggle_label(self, show, label):
		label.set_reveal_child(show)

	def show_internal_error_popup(self):
		dialog = Gtk.MessageDialog(parent=self.w, message_type=Gtk.MessageType.ERROR, 
			buttons=Gtk.ButtonsType.CLOSE, text="Internal Error")
		dialog.format_secondary_text("There was an error while accessing the database")
		dialog.run()
		dialog.destroy()

	def show_invalid_date_popup(self):
		dialog = Gtk.MessageDialog(parent=self.w, message_type=Gtk.MessageType.WARNING, 
			buttons=Gtk.ButtonsType.CLOSE, text="Invalid Date")
		dialog.format_secondary_text("Some dates introduced are incorrect")
		dialog.run()
		dialog.destroy()

	def show_about_popup(self):
		aboutPixbuf = GdkPixbuf.Pixbuf.new_from_file_at_scale(filename='assets/covid2.png', width=70, height=70, preserve_aspect_ratio=True)
		aboutPopup = Gtk.AboutDialog(title="About", program_name="Covid Radar", version="Version 1.0", logo=aboutPixbuf, parent=self.w)
		aboutPopup.set_comments("A simple desktop application to show covid related information about a person and its contacts between two dates.")
		aboutPopup.set_authors(["Julián Villamor", "Jorge Hermo", "David Rodríguez"])
		aboutPopup.set_website("https://github.com/daavidrgz/gtheme")
		aboutPopup.set_website_label("More interesting projects")
		aboutPopup.run()
		aboutPopup.destroy()

	def set_callbacks(self, ctr):
		self.searchEntry.connect("activate", ctr.search_users, self.searchEntry, ctr.search_accesses, 'search-window')
		self.searchEntry.connect("search-changed", ctr.search_changed, self.searchLabelRevealer)

		self.submitButton.connect("clicked", ctr.search_users, self.contactSearchEntry, ctr.search_contacts, 'contacts-window')
		self.contactSearchEntry.connect("activate", ctr.search_users, self.contactSearchEntry, ctr.search_contacts, 'contacts-window')
		self.contactSearchEntry.connect("search-changed", ctr.search_changed, self.contactsLabelRevealer)

		self.startDateEntry.connect("changed", ctr.start_date_changed)
		self.endDateEntry.connect("changed", ctr.end_date_changed)

		self.aboutButton.connect("clicked", ctr.show_about_popup)
