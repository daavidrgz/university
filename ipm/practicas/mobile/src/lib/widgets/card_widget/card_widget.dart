import 'package:flutter/material.dart';

class CardWidget extends StatelessWidget {
  final String text;
  final Icon icon;
  final VoidCallback callback;
  final Color color;
  final double width;
  final double height;

  const CardWidget({Key? key,
    required this.icon,
    required this.text,
    required this.callback,
    required this.color,
    required this.height,
    required this.width,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;

    return Container(
      width: size.width * width,
      height: size.height * height,
      margin: EdgeInsets.symmetric(vertical: size.height*0.02),
      decoration: BoxDecoration(
        color: color,
        borderRadius: BorderRadius.circular(size.width * 0.05),
        boxShadow: const [
          BoxShadow(
            color: Colors.black12,
            blurRadius: 4,
          )
        ]
      ),
      child: Material(
        type: MaterialType.transparency,
        color: Colors.white,
        shadowColor: Colors.black12,
        child: InkWell(
          onTap: callback,
          borderRadius:BorderRadius.circular(size.width * 0.05),
          child: Center(
            child: Row(
              children: [
                Flexible(
                  flex: 2,
                  child: Center(
                    child: Container(
                      width: size.height * 0.04,
                      height: size.height * 0.04,
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(size.width ),
                        color: const Color(0xFF3D56B2),
                      ),
                      child: Center(
                        child: icon,
                      )
                    ),
                  ),
                ),
                Flexible(
                  flex: 5,
                  child: Text(
                    text,
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      color: const Color(0xFF424874),
                      fontWeight: FontWeight.w500,
                      fontSize: size.width * 0.05,
                    ),
                  ),
                ),
                Flexible(
                  flex: 2,
                  child: Align(
                    alignment: Alignment.centerRight,
                    child: Icon(
                      Icons.arrow_forward_ios_rounded,
                      size: size.height * 0.025
                    )
                  )
                ),
              ]
            ),
          ),
        ),
      ),
    );
  }
}
