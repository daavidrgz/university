import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:flutter_application_1/model/model.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class PeopleCounter extends StatelessWidget {
  const PeopleCounter({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;

    return Container(
      decoration: BoxDecoration(
        color: const Color(0xFFD7E9F7),
        borderRadius: BorderRadius.circular(size.width * 0.03),
        boxShadow: const [
          BoxShadow(
            color: Colors.black12,
            blurRadius: 4,
          )
        ]
      ),
      width: size.width * 0.35,
      child: Column(
        children: <Widget>[
          Flexible(
            flex: 8,
            child: Center(
              child: Container(
                  width: size.height * 0.1,
                  height: size.height * 0.1,
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(size.width),
                    color: Colors.white,
                  ),
                  child: Center(
                    child: Icon(
                      Icons.people,
                      color: const Color(0xFF553C8B),
                      size: size.height *0.04
                    ),
                  )
                ),
              )
          ),
          Flexible(
            flex: 3,
            child: Text(
              AppLocalizations.of(context)!.peopleInside,
              textAlign: TextAlign.center,
              style: TextStyle(
                color: const Color(0xFF6B7AA1),
                fontSize: size.height * 0.02,
              ),
            )
          ),
          const SizedBox(height: 10),
          Flexible(
            flex: 2,
            child: Consumer<AccessModel>(
              builder: (context, accessModel, child) {
                int people = accessModel.inside.length;
                return Text(
                  "$people",
                  textAlign: TextAlign.center,
                  style: TextStyle(
                    color: const Color(0xFF553C8B),
                    fontWeight: FontWeight.bold,
                    fontSize: size.height * 0.03,
                  ),
                );
              }
            )
          )
        ],
      ),
    );
  }
}
