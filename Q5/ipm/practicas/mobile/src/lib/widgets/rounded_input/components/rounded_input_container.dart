import 'package:flutter/material.dart';


class RoundedInputContainer extends StatelessWidget{
  final Widget child;
  final double width;
  final double height;

  const RoundedInputContainer({Key? key, required this.child, required this.width, required this.height}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;

    return Container(
      margin: const EdgeInsets.symmetric(vertical: 10),
      padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 5),
      width: size.width * width,
      height: size.height * height,
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(8),
        boxShadow: const [
          BoxShadow(
            color: Color.fromRGBO(143, 148, 251, .2),
            blurRadius: 12,
            offset: Offset(0, 10)
          )
        ]
      ),
      child: child,
    );
  }
}
