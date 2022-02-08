import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_application_1/pages/create_access/create_access.dart';
import 'package:flutter_application_1/pages/people_list/people_list.dart';
import 'package:flutter_application_1/widgets/event_info/event_info.dart';
import 'package:flutter_application_1/widgets/info_widget/event_chronometer_card.dart';
import 'package:flutter_application_1/widgets/info_widget/people_counter_card.dart';
import 'package:flutter_application_1/widgets/popup/popup.dart';
import 'package:flutter_application_1/widgets/card_widget/card_widget.dart';
import 'package:flutter_application_1/model/model.dart';
import 'package:provider/provider.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';


class MainPage extends StatelessWidget {
  final String eventName;
  final String eventLocation;

  const MainPage({
    Key? key,
    required this.eventName,
    required this.eventLocation
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {

    Size size = MediaQuery.of(context).size;

    void _openNewAccess() {
      Navigator.push(context, MaterialPageRoute(
          builder: (c) => ChangeNotifierProvider.value(
            value: Provider.of<AccessModel>(context, listen: false),
            child: CreateAccess(),
          )
        )
      );
    }

    Future<void> _openPeopleList() async {
      await Provider.of<AccessModel>(context, listen: false).updateAccess();
      Navigator.push(context, MaterialPageRoute(
          builder: (c) => ChangeNotifierProvider.value(
            value: Provider.of<AccessModel>(context, listen: false),
            child: const PeopleList(),
          )
        )
      );
    }

    return Scaffold(
      backgroundColor: const Color(0xFF3D56B2),
      body: 
      Column(
        children: [
          Flexible(
            flex: 1,
            child: EventInfo(
              eventName: eventName,
              eventLocation: eventLocation,
            ),
          ),
          Flexible(
            flex: 5,
            child: Container(
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.only(
                  topRight: Radius.circular(size.width * 0.06),
                  topLeft: Radius.circular(size.width * 0.06)
                ),
              ),
              child: Center(
                child: Column(
                  children: [
                      Flexible(
                        flex: 2,
                        child: Center(
                          child: Padding(
                            padding: EdgeInsets.symmetric(vertical: size.height * 0.03),
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.center,
                              children: [
                                const PeopleCounter(),
                                Padding(padding: EdgeInsets.symmetric(horizontal: size.width * 0.05)),
                                const EventChronometer(),
                              ],
                            ),
                          ),
                        ),
                      ),
                      Flexible(
                        flex: 3,
                        child : Center(
                          child :Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            CardWidget(
                              icon: Icon(Icons.person_rounded, size: (size.height * 0.03), color: Colors.white),
                              text: AppLocalizations.of(context)!.manageAccesses,
                              color : Colors.white,
                              width: 0.8,
                              height: 0.09,
                              callback: _openNewAccess
                            ),
                            const Padding(
                              padding: EdgeInsets.symmetric(horizontal: 20)
                            ),
                            CardWidget(
                              icon: Icon(Icons.list_alt, size: (size.height * 0.03), color: Colors.white),
                              text: AppLocalizations.of(context)!.attendance,
                              width: 0.8,
                              height: 0.09,
                              color: Colors.white,
                              callback: ()async {
                                try{
                                  //Si no se puede actualizar de la BD, excepción
                                  await _openPeopleList();
                                }on SocketException catch(_) {
                                  showDialog<void>(
                                  context: context,
                                  builder: (BuildContext build) {
                                    FocusScope.of(context).unfocus();
                                    return PopUp(title: AppLocalizations.of(context)!.conexionError, text: AppLocalizations.of(context)!.conexionErrorSub);
                                    }
                                  );
                                }on InternalErrorException catch(_) {
                                  showDialog<void>(
                                  context: context,
                                  builder: (BuildContext build) {
                                    FocusScope.of(context).unfocus();
                                    return PopUp(title: AppLocalizations.of(context)!.internalError, text: AppLocalizations.of(context)!.internalErrorSub);
                                  }
                                );
                                }
                              }
                            ),
                          ]
                        ),
                      ),
                    ),
                    Flexible(
                      flex: 1,
                      child: Center(
                        child: FloatingActionButton.extended(
                          onPressed: () {
                            if ( Provider.of<AccessModel>(context, listen: false).inside.isNotEmpty ) {
                              showDialog<void>(
                                context: context,
                                builder: (BuildContext build) {
                                  FocusScope.of(context).unfocus();
                                  return PopUp(title: AppLocalizations.of(context)!.couldntEnd, text: AppLocalizations.of(context)!.couldntEndSub);
                                }
                              );
                            } else {
                              Provider.of<EventModel>(context,listen:false).stopEvent();
                            }
                          },
                          tooltip: AppLocalizations.of(context)!.endEvent,
                          backgroundColor: Colors.red,
                          label: Text(AppLocalizations.of(context)!.endEvent, style: TextStyle(fontSize: size.height * 0.02)),
                          icon: Icon(Icons.close_outlined, size: size.height * 0.025),
                        )
                      ),
                    ),
                  ]
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
