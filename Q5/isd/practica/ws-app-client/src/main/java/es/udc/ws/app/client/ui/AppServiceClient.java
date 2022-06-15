package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.service.ClientTripService;
import es.udc.ws.app.client.service.ClientTripServiceFactory;
import es.udc.ws.app.client.service.dto.ClientBookingDto;
import es.udc.ws.app.client.service.dto.ClientTripDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AppServiceClient {
    public static void main(String[] args) {

        if(args.length == 0) {
            printUsageAndExit();
        }
        ClientTripService clientTripService = ClientTripServiceFactory.getService();

        if(compareArgs("-a","--add-trip",args[0]))
            addTrip(args,clientTripService);
        else if(compareArgs("-b","--book",args[0]))
            book(args, clientTripService);
        else if(compareArgs("-u","--update-trip",args[0]))
            updateTrip(args, clientTripService);
        else if(compareArgs("-c","--cancel",args[0]))
            cancel(args , clientTripService);
        else if(compareArgs("-ft","--find-trips",args[0]))
            findTrips(args, clientTripService);
        else if(compareArgs("-fb","--find-bookings",args[0]))
            findBookings(args,clientTripService);
        else
            printUsage();

    }
    private static boolean compareArgs(String shortcut,String complete,String actual){
        return shortcut.equalsIgnoreCase(actual) || complete.equalsIgnoreCase(actual);
    }

    private static void addTrip(String[] args, ClientTripService clientTripService){
        validateArgs(args,6,new int[]{4,5});
        //[add] -a,  --add-trip <city> <description> <date> <price> <maxPlaces>

        try{
            ClientTripDto trip = new ClientTripDto(null,
                    args[1],args[2],LocalDateTime.parse(args[3]),
                    Double.parseDouble(args[4]),Integer.parseInt(args[5]),null
                    );
            ClientTripDto addedTrip = clientTripService.addTrip(trip);

            System.out.println("Trip "+addedTrip.getId()+" created successfully");
        } catch(Exception ex){
            ex.printStackTrace(System.err);
        }
    }
    private static void book(String[] args, ClientTripService clientTripService) {
        // [book] -b, --book <userEmail> <tripId> <creditCardNumber> <places>
        validateArgs(args, 5, new int[] {2, 4});

        try{
            Long bookingId = clientTripService.bookTrip(Long.parseLong(args[2]), args[1],
                    Integer.parseInt(args[4]), args[3]);

            System.out.println("Successfully booked trip " + args[2] +
                    " with booking id: " + bookingId);
        } catch(Exception e) {
            e.printStackTrace(System.err);
        }
    }
    private static void updateTrip(String[] args, ClientTripService clientTripService){
        // [update] -u, --update-trip <tripId> <city> <description> <date> <price> <maxPlaces>
        validateArgs(args, 7, new int[] {1, 5, 6});

        try {
            clientTripService.updateTrip(new ClientTripDto(
                    Long.parseLong(args[1]), args[2], args[3],
                    LocalDateTime.parse(args[4]), Double.parseDouble(args[5]),
                    Integer.parseInt(args[6]), 0));

            System.out.println("Successfully updated trip " + args[1]);
        } catch(Exception e) {
            e.printStackTrace(System.err);
        }
    }
    private static void cancel(String[] args, ClientTripService clientTripService){
        //[cancel] -c,  --cancel <bookingId> <userEmail>2
        validateArgs(args, 3, new int[] {1});
        try{
            clientTripService.cancelBooking(Long.parseLong(args[1]),args[2]);

            System.out.println("booking " + args[1] +
                    " cancelled successfully");


        } catch(Exception ex){
            ex.printStackTrace(System.err);
        }

    }
    private static void findTrips(String[] args, ClientTripService clientTripService){
        //[find]   -ft, --find-trips <city> <fromDate> <toDate>
        validateArgs(args, 4, new int[] {});

        try{
            List<ClientTripDto> trips = clientTripService.findTripByCity( args[1], LocalDate.parse(args[2]),LocalDate.parse(args[3]));
            if(trips.isEmpty()){
                System.out.println("There are no trips to show");
            }else{
                System.out.println("Requested trips are:");
                for(ClientTripDto trip : trips){
                    System.out.println(" * " + trip);
                }
            }

        }catch(Exception ex){
            ex.printStackTrace(System.err);

        }

    }
    private static void findBookings(String[] args, ClientTripService clientTripService){
        //[find]   -fb, --find-bookings <userEmail>
        validateArgs(args,2,new int[]{});
        try{
            List<ClientBookingDto> bookings = clientTripService.getBookings(args[1]);
            if(bookings.isEmpty()){
                System.out.println("There are no bookings to show");
            }else{
                System.out.println("Requested bookings are:");
                for(ClientBookingDto booking : bookings){
                    System.out.println(" * "+booking);
                }
            }

        }catch(Exception ex){
            ex.printStackTrace(System.err);
        }
    }



    public static void validateArgs(String[] args, int expectedArgs,
                                    int[] numericArguments) {
        if(expectedArgs != args.length) {
            printUsageAndExit();
        }
        for (int position : numericArguments) {
            try {
                Double.parseDouble(args[position]);
            } catch (NumberFormatException n) {
                printUsageAndExit();
            }
        }
    }

    public static void printUsageAndExit(){
        printUsage();
        System.exit(-1);
    }

    public static void printUsage(){
        System.err.println("Usage:\n" +
                "   [add]    -a,  --add-trip <city> <description> <date> <price> <maxPlaces>\n" +
                "   [book]   -b,  --book <userEmail> <tripId> <creditCardNumber> <places>\n" +
                "   [update] -u,  --update-trip <tripId> <city> <description> <date> <price> <maxPlaces>\n" +
                "   [cancel] -c,  --cancel <bookingId> <userEmail>\n" +
                "   [find]   -ft, --find-trips <city> <fromDate> <toDate>\n" +
                "   [find]   -fb, --find-bookings <userEmail>\n"
                );
    }
}
