import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:flutter_application_1/pages/main_page/main_page.dart';
import 'package:flutter_application_1/pages/start_page/start_page.dart';
import 'package:flutter_application_1/model/model.dart';

class Root extends StatelessWidget {
  const Root({Key? key}) : super(key: key);
  
  @override
  Widget build(BuildContext context){
    return Consumer<EventModel>(
      builder: (context, eventModel, child) {
        return Container(
          color: (eventModel.started) ? Colors.white : const Color(0xFF3D56B2),
          child: AnimatedSwitcher(
            duration: const Duration(milliseconds:500),
            transitionBuilder: (child, animation) {
              const begin = Offset(0.0, 1.0);
              const end = Offset.zero;
              const curve = Curves.ease;

              final tween = Tween(begin: begin, end: end).chain(CurveTween(curve: curve));

              return SlideTransition(
                position: animation.drive(tween),
                child: child,
              );
            },
            child: ( eventModel.started ) ?
              MainPage(eventName: eventModel.eventName, eventLocation: eventModel.eventLocation)
              :
              StartPage()
          ),
        );
      }
    );
  }
}
