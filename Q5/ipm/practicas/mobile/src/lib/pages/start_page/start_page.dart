import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter_application_1/widgets/popup/popup.dart';
import 'package:flutter_application_1/widgets/rounded_input/rounded_date_input.dart';
import 'package:flutter_application_1/widgets/rounded_input/rounded_input.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter_application_1/widgets/submit_button/submit_button.dart';
import 'package:provider/provider.dart';
import 'package:flutter_application_1/model/model.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class StartPage extends StatelessWidget {
  final eventNameController = TextEditingController();
  final eventLocationController = TextEditingController();
  final eventStartTimeController = TextEditingController();


  StartPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () => FocusScope.of(context).unfocus(),
      child: Scaffold(
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              RoundedInput(placeholder: AppLocalizations.of(context)!.event,controller:eventNameController, width: 0.8, height: 0.07),
              RoundedInput(placeholder: AppLocalizations.of(context)!.facility,controller:eventLocationController, width: 0.8, height: 0.07),
              RoundedDateInput(placeholder:AppLocalizations.of(context)!.eventStartDate, controller: eventStartTimeController, width: 0.8, height: 0.08),
              //Fecha de fin de evento -> default now
              SubmitButton(
                text: AppLocalizations.of(context)!.startEvent,
                height: 0.07,
                width: 0.6,
                function: () async {
                  try{
                    FocusScope.of(context).unfocus();
                    EventModel eventModel = Provider.of<EventModel>(context,listen:false);
                    await eventModel.startEvent(eventNameController.text,eventLocationController.text,
                       ( eventStartTimeController.text == "" ) ? null : DateTime.parse(eventStartTimeController.text));
                    // Inicializar el id para el modelo de accesos
                    Provider.of<AccessModel>(context,listen:false)
                        .initEvent(eventModel.locationId,eventModel.startTime!);
                    await Provider.of<AccessModel>(context,listen:false)
                        .updateAccess();
                  } on LocationNotFoundException catch(_){
                    showDialog<void>(
                      context: context,
                      builder: (BuildContext build) {
                        FocusScope.of(context).unfocus();
                        return  PopUp(title: AppLocalizations.of(context)!.facilityNotFound, text: AppLocalizations.of(context)!.facilityNotFoundSub);
                      }
                    );
                  } on SocketException catch(_) {
                    showDialog<void>(
                      context: context,
                      builder: (BuildContext build) {
                      FocusScope.of(context).unfocus();
                      return PopUp(title: AppLocalizations.of(context)!.conexionError, text: AppLocalizations.of(context)!.conexionErrorSub);
                      }
                    );
                  } on InternalErrorException catch(_) {
                    showDialog<void>(
                      context: context,
                      builder: (BuildContext build) {
                        FocusScope.of(context).unfocus();
                        return PopUp(title: AppLocalizations.of(context)!.internalError, text: AppLocalizations.of(context)!.internalErrorSub);
                      }
                    );
                  }
                }
              )
            ]
          ),
        ),
      ),
    );
  }
}
