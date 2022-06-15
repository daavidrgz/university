import 'package:flutter/material.dart';
import 'package:flutter_application_1/model/model.dart';
import 'package:flutter_application_1/widgets/info_widget/components/stop_watch.dart';
import 'package:provider/provider.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class EventChronometer extends StatelessWidget {
  const EventChronometer({Key? key}) : super(key: key);

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
                width: size.height*0.1,
                height: size.height*0.1,
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(size.width),
                  color: Colors.white,
                ),
                child: Center(
                  child: Icon(
                    Icons.timer,
                    color: const Color(0xFF553C8B),
                    size: size.height * 0.04,
                  ),
                )
              ),
            )
          ),
          Flexible(
            flex: 3,
            child: Text(
              AppLocalizations.of(context)!.eventDuration,
              textAlign: TextAlign.center,
              style: TextStyle(
                color: const Color(0xFF69779B),
                fontSize: size.height * 0.02,
              ),
            )
          ),
          const SizedBox(height: 10),
          Flexible(
            flex: 2,
            child: StopWatch(startTime:Provider.of<EventModel>(context, listen: false).startTime!),
          )

        ],
      ),
    );
  }
}
