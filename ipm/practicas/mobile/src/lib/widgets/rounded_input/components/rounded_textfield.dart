import 'package:flutter/material.dart';

class RoundedTextField extends StatefulWidget {
  final String placeholder;
  final TextEditingController controller;

  const RoundedTextField({
    Key? key,
    required this.placeholder,
    required this.controller
  }) : super(key : key);

  @override
  State<RoundedTextField> createState() => _RoundedTextFieldState();
}

class _RoundedTextFieldState extends State<RoundedTextField> {
  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return TextField(
      controller: widget.controller,
      textAlign: TextAlign.center,
      style: TextStyle(fontSize: size.height*0.02, color: Colors.grey[600]),
      cursorColor:  Colors.grey[600],
      decoration: InputDecoration(
        hintText: widget.placeholder,
        hintStyle: TextStyle(fontSize: size.height*0.02, color: Colors.grey[400]),
        border:  InputBorder.none,
      ),
    );
    }
  }
