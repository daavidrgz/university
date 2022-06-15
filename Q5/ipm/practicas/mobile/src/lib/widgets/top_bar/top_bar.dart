import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class TopBar extends StatelessWidget {
  final String title;

  const TopBar({Key? key, required this.title}) : super(key: key);
  
  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;

    return Container(
      width: size.width,
      height: size.height * 0.08,
      decoration: const BoxDecoration(
        color: Colors.white,
        boxShadow: [
          BoxShadow(
            color: Colors.black12,
            blurRadius: 4,
          )
        ]
      ),
      child: Row(
        children: <Widget>[
          Material(
            type: MaterialType.transparency,
            color: Colors.white,
            shadowColor: Colors.black12,
            child: Container(
              padding: EdgeInsets.only(left: size.width * 0.05),
              child: InkWell(
                onTap: () { Navigator.of(context).maybePop(); },
                borderRadius: BorderRadius.circular(100),
                child: Icon(Icons.arrow_back_rounded,size: (size.height * 0.03))
              ),
            ),
          ),
          Container(
            padding: EdgeInsets.only(left: size.width * 0.05),
            child: Text(
              title,
              style: TextStyle(
                fontWeight: FontWeight.w600,
                fontSize: size.height * 0.026
              ),
            )
          )
        ],
      ),
    );
  }
}
