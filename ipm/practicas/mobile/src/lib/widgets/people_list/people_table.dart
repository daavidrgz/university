import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:flutter_application_1/model/model.dart';
import 'package:flutter_application_1/widgets/people_list/user_card.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class Dropdown extends StatefulWidget {
  final Function dropdownCallback;
  const Dropdown({Key? key, required this.dropdownCallback}) : super(key: key);

  @override
  State<Dropdown> createState() => _DropdownState();
}

class _DropdownState extends State<Dropdown> {
  String _chosenValue = "Actual";

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;

    return DropdownButton<String>(
      focusColor:Colors.white,
      value: _chosenValue,
      style: const TextStyle(color: Colors.white),
      items: [
        DropdownMenuItem<String>(
          value: 'Actual',
          child: Text(AppLocalizations.of(context)!.actual, style: TextStyle(color: Colors.black, fontWeight: FontWeight.w600, fontSize: size.height * 0.025)),
        ),
        DropdownMenuItem<String>(
          value: 'Total',
          child: Text(AppLocalizations.of(context)!.total, style: TextStyle(color: Colors.black, fontWeight: FontWeight.w600, fontSize: size.height * 0.025)),
        )
      ],
      onChanged: (String? value) async {
        widget.dropdownCallback(value);
        setState(() {
          _chosenValue = value!;
        });
      },
    );
  }
}

class TableHeaders extends StatelessWidget {
  final int people;
  final Function dropdownCallback;
  const TableHeaders({Key? key, required this.people, required this.dropdownCallback}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;

    return Container(
      padding: EdgeInsets.symmetric(horizontal: size.width * 0.08, vertical: size.height * 0.02),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Dropdown(dropdownCallback: dropdownCallback),
          Text('${AppLocalizations.of(context)!.total}: ' + people.toString(), style:  TextStyle(color: Colors.black, fontWeight: FontWeight.w500, fontSize: size.height * 0.025))
        ]
      )
    );
  }
}

class PeopleTable extends StatefulWidget {
  final Function newAccessCallback;
  const PeopleTable({Key? key, required this.newAccessCallback}) : super(key: key);

  @override
  State<PeopleTable> createState() => _PeopleTableState();
}

class _PeopleTableState extends State<PeopleTable> {
  String tableFilter = "Actual";

  void changeState(String filter) {
    setState(() {tableFilter = filter;});
  }

  @override
  Widget build(BuildContext context) {

    return Consumer<AccessModel>(
      builder: (context, accessModel, child) {
        

        List<Access> accessList = ( tableFilter == "Actual" ? accessModel.inside : accessModel.totalAccess );

        return Expanded(
          child: Column(
            children: [
              TableHeaders(people: accessList.length, dropdownCallback: changeState),
              Expanded(
                child: ListView(
                  physics: const AlwaysScrollableScrollPhysics(),
                  shrinkWrap: false,
                  children: accessList.map((access) => UserCard(name: access.name, surname: access.surname, 
                    uuid: access.uuid ?? "", inTime: access.inTime, outTime: access.outTime, 
                    newAccessCallback: widget.newAccessCallback)).toList()
                )
              )
            ]
          )
        );
      }
    );
  }
}
