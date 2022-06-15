import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class SubmitButton extends StatelessWidget{
  final double height;
  final double width;
  final VoidCallback function;
  final String text;

  const SubmitButton({
    Key? key,
    required this.height,
    required this.width,
    required this.function,
    required this.text
  }) : super(key: key);

  ButtonStyle buttonStyle() {
    return ButtonStyle(
      shape: MaterialStateProperty.all<RoundedRectangleBorder>(
        RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(8)
        )
      ),
      backgroundColor: MaterialStateProperty.all(Colors.transparent),
      shadowColor: MaterialStateProperty.all(Colors.transparent)
    );
  }

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return Container(
      width: size.width * width,
      height: size.height * height,
      decoration: BoxDecoration(
        gradient: const LinearGradient(
          begin: Alignment.topRight,
          end: Alignment.bottomLeft,
          colors: [Colors.lightBlue,Color(0xFF3D56B2)],
        ),
        borderRadius: BorderRadius.circular(8),
      ),
      child: ElevatedButton(
        onPressed: function,
        style: buttonStyle(),
        child: Text(
          text,
          textAlign: TextAlign.center,
          style: TextStyle(
            fontSize: size.height * 0.02,
          ),
        ),
      ),
    );
  }
}
