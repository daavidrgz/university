#! /usr/bin/python3

from model import Model
from controller import Controller
from view import View

import gi
gi.require_version("Gtk", "3.0")
from gi.repository import Gtk, Atk, GdkPixbuf

if __name__ == '__main__':
	Controller(View(), Model())
	Gtk.main()