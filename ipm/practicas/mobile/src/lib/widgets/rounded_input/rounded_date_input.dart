import 'package:flutter/material.dart';
import 'components/rounded_input_container.dart';
import 'components/rounded_date_selector.dart';

class RoundedDateInput extends StatelessWidget {
  final double width;
  final double height;
  final String placeholder;
  final TextEditingController controller;

  const RoundedDateInput({Key? key,
    required this.placeholder,
    required this.controller,
    required this.width,
    required this.height,
   }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return RoundedInputContainer(
      child: RoundedDateSelector(
        placeholder: placeholder,
        controller: controller
      ),
      height: height,
      width: width,
    );
  }




  }
