import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_application_1/widgets/people_list/people_table.dart';
import 'package:flutter_application_1/widgets/top_bar/top_bar.dart';
import 'package:flutter_application_1/pages/create_access/create_access.dart';
import 'package:flutter_application_1/model/model.dart';
import 'package:provider/provider.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class PeopleList extends StatelessWidget {
  const PeopleList({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {

    void openNewAccess(String name, String surname, String uuid) {
      Navigator.push(context, MaterialPageRoute(
          builder: (c) => ChangeNotifierProvider.value(
            value: Provider.of<AccessModel>(context, listen: false),
            child: CreateAccess(name: name, surname: surname, uuid: uuid, initialSwitchState: false)
          )
        )
      );
    }

    return Scaffold(
      backgroundColor: const Color(0xFFF9F9F9),
      body: Column(
        children: [
          TopBar(title: AppLocalizations.of(context)!.attendance),
          PeopleTable(newAccessCallback: openNewAccess)
        ],
      ),
    );
  }
}
