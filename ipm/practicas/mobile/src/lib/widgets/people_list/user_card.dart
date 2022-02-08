import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_application_1/widgets/submit_button/submit_button.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class UserCard extends StatelessWidget {
  final String name;
  final String surname;
  final String uuid;
  final DateTime? inTime;
  final DateTime? outTime;
  final Function? newAccessCallback;

  const UserCard({
    Key? key,
    required this.uuid,
    required this.name,
    required this.surname,
    required this.inTime,
    required this.outTime,
    required this.newAccessCallback,
  }) : super(key: key);

  String _formatDate(DateTime? date) {
    if ( date == null ) return "N/A";
    return "${date.hour.toString().padLeft(2,'0')}:${date.minute.toString().padLeft(2,'0')} • "
      "${date.day.toString().padLeft(2,'0')}-${date.month.toString().padLeft(2,'0')}-${date.year.toString()}";
  }

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;

    return Container(
      padding: EdgeInsets.all(size.width * 0.08),
      decoration: const BoxDecoration(
        border: Border(bottom: BorderSide(width: 1, color: Colors.black12))
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Flexible(
            flex: 4,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  surname + ', ' + name, 
                  style: TextStyle(fontWeight: FontWeight.w600, fontSize: size.height * 0.022 )
                ),
                const SizedBox(height: 2),
                SingleChildScrollView(
                  scrollDirection: Axis.horizontal,
                  child: Text('UUID: ' + uuid, style: TextStyle(fontSize: size.height * 0.02)),
                ),
                const SizedBox(height: 2),
                SingleChildScrollView(
                  scrollDirection: Axis.horizontal,
                  child: Text('${AppLocalizations.of(context)!.entry}: ' + _formatDate(inTime), style: TextStyle(fontSize: size.height * 0.02)),
                ),
                const SizedBox(height: 2),
                SingleChildScrollView(
                  scrollDirection: Axis.horizontal,
                  child: Text('${AppLocalizations.of(context)!.exit}: ' + _formatDate(outTime), style: TextStyle(fontSize: size.height * 0.02)),
                ),
              ]
            ),
          ),
          Flexible(
            flex: 3,
            child: ( outTime == null ) ?
              SubmitButton(
                height: 0.06,
                width: 0.27,
                text: AppLocalizations.of(context)!.registerExit,
                function: () {newAccessCallback!(name, surname, uuid);}
              )
              : 
              Container()
          ),
        ]
      )
    );
  }
}
