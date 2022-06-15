import 'package:flutter/material.dart';

class EventInfo extends StatelessWidget {
  final String eventName;
  final String eventLocation;

  const EventInfo({
    Key? key,
    required this.eventName,
    required this.eventLocation
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Flexible(
            flex: 8,
            child: Text(
              eventName,
              textAlign: TextAlign.center,
              style: TextStyle(
                fontWeight: FontWeight.bold,
                color: Colors.white,
                fontSize: size.width * 0.08,
              ),
            )
          ), 
          Flexible(
            flex: 6,
            child: Text(
              eventLocation,
              textAlign: TextAlign.center,
              style: TextStyle(
                fontWeight: FontWeight.bold,
                color: Colors.white,
                fontSize: size.width * 0.04,
              ),
            )
          ),
        ],
      )
    );
  }
}
