import 'dart:io';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_application_1/model/model.dart';
import 'package:flutter_application_1/pages/qr_scanner/qr_scanner.dart';
import 'package:flutter_application_1/widgets/popup/popup.dart';
import 'package:flutter_application_1/widgets/rounded_input/rounded_input.dart';
import 'package:flutter_application_1/widgets/rounded_input/rounded_date_input.dart';
import 'package:flutter_application_1/widgets/submit_button/submit_button.dart';
import 'package:flutter_application_1/widgets/top_bar/top_bar.dart';
import 'package:provider/provider.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class TempInvalid implements Exception {
  String errMsg() => "No Valid Temperature";
}

class AccessSwitch extends StatefulWidget {
  final bool initialState;
  final Function switchCallback;
  const AccessSwitch({Key? key, required this.switchCallback, this.initialState = true}) : super(key: key);

  @override
  State<AccessSwitch> createState() => _AccessSwitchState();
}

class _AccessSwitchState extends State<AccessSwitch> {
  late bool isEntry;

  @override
  void initState() {
    super.initState();
    isEntry = widget.initialState;
  }

  @override Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;

    return Center(
      child: CupertinoSlidingSegmentedControl(
        onValueChanged: (value) {
          widget.switchCallback(); 
          setState(() {isEntry = !isEntry;});
        },
        children: {
          'IN':  Text(AppLocalizations.of(context)!.entry, style: TextStyle(fontSize: size.height * 0.025)),
          'OUT':  Text(AppLocalizations.of(context)!.exit, style: TextStyle(fontSize: size.height * 0.025)),
        },
        groupValue: isEntry ? "IN" : "OUT",
      ),
    );
  }
}

class CreateAccess extends StatefulWidget {
  final TextEditingController name;
  final TextEditingController surname;
  final TextEditingController uuid;
  final phone = TextEditingController();
  final accessDate = TextEditingController();
  final temperature = TextEditingController();
  final bool initialSwitchState;

  CreateAccess({
    Key? key,
    String? name,
    String? surname,
    String? uuid,
    this.initialSwitchState = true
  }) :
  name = TextEditingController(text: name),
  surname = TextEditingController(text: surname),
  uuid = TextEditingController(text: uuid), 
  super(key: key);

  @override
  State<CreateAccess> createState() => _CreateAccessState();
}

class _CreateAccessState extends State<CreateAccess> {
  late bool isEntry;

  @override
  void initState() {
    super.initState();
    isEntry = widget.initialSwitchState;
  }

  void changeState() {
    setState(() {isEntry = !isEntry;});
  }

  double? formatTemp() {
    try {
      if ( widget.temperature.text == "" ) return null;
      return double.parse(widget.temperature.text);
    } on FormatException catch(_) {
      throw TempInvalid();
    }
  }

  @override
  Widget build(BuildContext context){

    Size size = MediaQuery.of(context).size;

    Future<Access> createAccess() async {
      Access access;
      if ( isEntry ) {
        access = Access(
          name: widget.name.text,
          surname: widget.surname.text,
          uuid: widget.uuid.text,
          phone: widget.phone.text,
          inTemperature: formatTemp(),
          inTime: ( widget.accessDate.text == "" ) ? null : DateTime.parse(widget.accessDate.text)
        );
      } else {
          access = Access(
          name: widget.name.text,
          surname: widget.surname.text,
          uuid: widget.uuid.text,
          phone: widget.phone.text,
          outTemperature: formatTemp(),
          outTime: ( widget.accessDate.text == "" ) ? null : DateTime.parse(widget.accessDate.text)
        );
      }
      await access.correctUser();
      return access;
    }

    void _openScanner() {
      Navigator.push(context, MaterialPageRoute(builder: (c) => Scanner(name: widget.name, surname: widget.surname,uuid: widget.uuid)));
    }

    return GestureDetector(
        onTap: () => FocusScope.of(context).unfocus(),
          child: Scaffold(
            backgroundColor: const Color(0xFF3D56B2),
            body: Column(
              children: [
                 Flexible(
                  flex: 1,
                  child: TopBar(title: AppLocalizations.of(context)!.registerAccess),
                ),
                Flexible(
                  flex: 12,
                  child: Container(
                    decoration: const BoxDecoration(
                      color: Color(0xFFF9F9F9),
                    ),
                    child: Center(
                      child: SingleChildScrollView(
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            AccessSwitch(switchCallback: changeState, initialState: widget.initialSwitchState),
                            SizedBox(height: size.height * 0.03),
                            Row(
                              mainAxisAlignment: MainAxisAlignment.center,
                              children: [
                                Flexible(flex: 5, child:RoundedInput(placeholder: "${AppLocalizations.of(context)!.name}*", controller: widget.name,width: 0.35, height: 0.07)),
                                Flexible(flex: 1, child: Container()),
                                Flexible(flex: 5, child:RoundedInput(placeholder: "${AppLocalizations.of(context)!.surname}*", controller: widget.surname,width: 0.35, height: 0.07))
                              ]
                            ),
                            RoundedInput(placeholder: "UUID*", controller: widget.uuid, width: 0.8, height: 0.08),
                            RoundedInput(placeholder: "${AppLocalizations.of(context)!.phone}*", controller: widget.phone, width: 0.8, height: 0.08),
                            RoundedDateInput(placeholder: isEntry ? AppLocalizations.of(context)!.entryDate : AppLocalizations.of(context)!.exitDate, controller: widget.accessDate, width: 0.8, height: 0.08),
                            RoundedInput(placeholder: "${AppLocalizations.of(context)!.temperature}* (°C)", controller: widget.temperature, width: 0.5, height: 0.08),
                            SizedBox(height: size.height * 0.04),
                            SubmitButton(text: isEntry?AppLocalizations.of(context)!.registerEntry:AppLocalizations.of(context)!.registerExit,height: 0.07, width: 0.6, function: () async {
                              try {
                                await Provider.of<AccessModel>(context, listen: false).registerAccess(
                                  isEntry ? "IN" : "OUT", await createAccess());
                                Navigator.of(context).maybePop();
                              } on TempInvalid catch(_) {
                                showDialog<void>(
                                  context: context,
                                  builder: (BuildContext build) {
                                    FocusScope.of(context).unfocus();
                                    return PopUp(title: AppLocalizations.of(context)!.invalidTemperature, text: AppLocalizations.of(context)!.invalidTemperatureSub);
                                  }
                                );
                              } on TemperatureNotEspecifiedException catch(_) {
                                showDialog<void>(
                                  context: context,
                                  builder: (BuildContext build) {
                                    FocusScope.of(context).unfocus();
                                    return PopUp(title: AppLocalizations.of(context)!.temperatureNotEspecified, text: AppLocalizations.of(context)!.temperatureNotEspecifiedSub);
                                  }
                                );
                              } on NoMatchedUserException catch(_) {
                                showDialog<void>(
                                  context: context,
                                  builder: (BuildContext build) {
                                    FocusScope.of(context).unfocus();
                                    return PopUp(title: AppLocalizations.of(context)!.noMatchedUser, text: AppLocalizations.of(context)!.noMatchedUserSub);
                                  }
                                );
                              } on NoIdentificationEspecifiedException catch(_) {
                                showDialog<void>(
                                  context: context,
                                  builder: (BuildContext build) {
                                    FocusScope.of(context).unfocus();
                                    return PopUp(title: AppLocalizations.of(context)!.noIdentification, text: AppLocalizations.of(context)!.noIdentificationSub);
                                  }
                                );
                              } on AlreadyInsideException catch(_) {
                                showDialog<void>(
                                  context: context,
                                  builder: (BuildContext build) {
                                    FocusScope.of(context).unfocus();
                                    return PopUp(title: AppLocalizations.of(context)!.alreadyInside, text: AppLocalizations.of(context)!.alreadyInsideSub);
                                  }
                                );
                              } on NotInsideException catch(_) {
                                showDialog<void>(
                                  context: context,
                                  builder: (BuildContext build) {
                                    FocusScope.of(context).unfocus();
                                    return PopUp(title: AppLocalizations.of(context)!.notInside, text: AppLocalizations.of(context)!.notInsideSub);
                                  }
                                );
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
                            }),
                            SizedBox(height: size.height * 0.04),
                            FloatingActionButton(
                              onPressed: () => _openScanner(),
                              tooltip: AppLocalizations.of(context)!.qrinfo,
                              child: const Icon(Icons.qr_code),
                            )
                          ]
                        )
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ),// This trailing comma makes auto-formatting nicer for build methods.
         );
      }
}
