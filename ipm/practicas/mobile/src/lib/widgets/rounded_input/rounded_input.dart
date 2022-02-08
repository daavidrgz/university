import 'package:flutter/material.dart';
import 'components/rounded_input_container.dart';
import 'components/rounded_textfield.dart';

class RoundedInput extends StatelessWidget {
  final double width;
  final double height;
  final String placeholder;
  final TextEditingController controller;

  const RoundedInput({Key? key,
    required this.placeholder,
    required this.controller,
    required this.width,
    required this.height,
   }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return RoundedInputContainer(
      child: Center(
        child: RoundedTextField(
        controller: controller,
        placeholder: placeholder,
        ),
      ),
      height: height,
      width: width,
    );
  }




  }
