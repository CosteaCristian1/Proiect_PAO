    import java.text.SimpleDateFormat;
    import java.util.*;

    class Utilizator {
        private Integer id;
        private String nume;
        private String email;

        public Utilizator(Integer id, String nume, String email) {
            this.nume = nume;
            this.email = email;
        }

        public String getNume() {
            return nume;
        }

        public String getEmail() {
            return email;
        }
    }

    class Pasager extends Utilizator {
        private List<Rezervare> rezervari;

        public Pasager(Integer id, String nume, String email) {
            super(id, nume, email);
            this.rezervari = new ArrayList<>();
        }

        public void adaugaRezervare(Rezervare rezervare) {
            rezervari.add(rezervare);
        }

        public void eliminaRezervare(Rezervare rezervare) {
            rezervari.remove(rezervare);
        }

        public List<Rezervare> getRezervari() {
            return rezervari;
        }
    }

    class Aeroport {
        private String codIATA;
        private String nume;
        private String locatie;

        public Aeroport(String codIATA, String nume, String locatie) {
            this.codIATA = codIATA;
            this.nume = nume;
            this.locatie = locatie;
        }

        public String getCodIATA() {
            return codIATA;
        }

        public String getNume() {
            return nume;
        }

        public String getLocatie() {
            return locatie;
        }
    }

    class Zbor implements Comparable<Zbor> {
        private String codZbor;
        private Aeroport plecare;
        private Aeroport destinatie;
        private Date dataPlecare;
        private int locuriDisponibile;

        public Zbor(String codZbor, Aeroport plecare, Aeroport destinatie, Date dataPlecare, int locuriDisponibile) {
            this.codZbor = codZbor;
            this.plecare = plecare;
            this.destinatie = destinatie;
            this.dataPlecare = dataPlecare;
            this.locuriDisponibile = locuriDisponibile;
        }

        public String getCodZbor() {
            return codZbor;
        }

        public Aeroport getPlecare() {
            return plecare;
        }

        public Aeroport getDestinatie() {
            return destinatie;
        }

        public Date getDataPlecare() {
            return dataPlecare;
        }

        public int getLocuriDisponibile() {
            return locuriDisponibile;
        }

        public void rezervaLocuri(int nrLocuri) {
            if (nrLocuri <= locuriDisponibile) {
                locuriDisponibile -= nrLocuri;
            } else {
                System.out.println("Nu sunt suficiente locuri disponibile.");
            }
        }

        @Override
        public int compareTo(Zbor other) {
            return this.dataPlecare.compareTo(other.dataPlecare);
        }
    }

    class Rezervare {
        private Zbor zbor;
        private Pasager pasager;
        private Bilet bilet;
        private List<Bagaj> bagaje;
        private static int idCounter = 0;
        private int id;

        public Rezervare(Zbor zbor, Pasager pasager) {
            this.zbor = zbor;
            this.pasager = pasager;
            this.bagaje = new ArrayList<>();
            this.bilet = new Bilet(zbor, pasager);
            this.id = ++idCounter;
        }

        public void adaugaBagaj(Bagaj bagaj) {
            bagaje.add(bagaj);
        }

        public void eliminaBagaj(Bagaj bagaj) {
            bagaje.remove(bagaj);
        }

        public Zbor getZbor() {
            return zbor;
        }

        public Pasager getPasager() {
            return pasager;
        }

        public List<Bagaj> getBagaje() {
            return bagaje;
        }

        public Bilet getBilet() {
            return bilet;
        }

        public int getId() {
            return id;
        }
    }

    class Bilet {
        private Zbor zbor;
        private Pasager pasager;

        public Bilet(Zbor zbor, Pasager pasager) {
            this.zbor = zbor;
            this.pasager = pasager;
        }

        public Zbor getZbor() {
            return zbor;
        }

        public Pasager getPasager() {
            return pasager;
        }

        public void afisareDetalii() {
            System.out.println("Bilet pentru zborul " + zbor.getCodZbor() + " cu destinatia " + zbor.getDestinatie().getNume());
            System.out.println("Pasager: " + pasager.getNume());
        }
    }

    class Bagaj {
        private double greutate;
        private String tip;

        public Bagaj(double greutate, String tip) {
            this.greutate = greutate;
            this.tip = tip;
        }

        public double getGreutate() {
            return greutate;
        }

        public String getTip() {
            return tip;
        }
    }

    class ServiciuRezervare {
        private List<Zbor> zboruri;
        private Set<Pasager> pasageri;
        private Map<Integer, Rezervare> rezervari;
        private List<Aeroport> aeroporturi;

        public ServiciuRezervare() {
            this.zboruri = new ArrayList<>();
            this.pasageri = new HashSet<>();
            this.rezervari = new HashMap<>();
            this.aeroporturi = new ArrayList<>();
        }

        public void adaugaAeroport(Aeroport aeroport) {
            aeroporturi.add(aeroport);
        }

        public void adaugaZbor(Zbor zbor) {
            zboruri.add(zbor);
        }

        public List<Aeroport> getAeroporturi() {
            return aeroporturi;
        }

        public void adaugaPasager(Pasager pasager) {
            pasageri.add(pasager);
        }

        public List<Zbor> cautaZboruri(String plecare, String destinatie) {
            List<Zbor> rezultate = new ArrayList<>();
            for (Zbor zbor : zboruri) {
                if (zbor.getPlecare().equals(plecare) && zbor.getDestinatie().equals(destinatie)) {
                    rezultate.add(zbor);
                }
            }
            return rezultate;
        }

        public void rezervaZbor(Pasager pasager, Zbor zbor) {
            if (zbor.getLocuriDisponibile() > 0) {
                Rezervare rezervare = new Rezervare(zbor, pasager);
                pasager.adaugaRezervare(rezervare);
                zbor.rezervaLocuri(1);
                rezervari.put(rezervare.getId(), rezervare);
                System.out.println("Rezervare completă pentru " + pasager.getNume());
            } else {
                System.out.println("Nu mai sunt locuri disponibile pe acest zbor.");
            }
        }

        public void anuleazaRezervare(int idRezervare) {
            Rezervare rezervare = rezervari.get(idRezervare);
            if (rezervare != null) {
                rezervare.getZbor().rezervaLocuri(-1);
                rezervare.getPasager().eliminaRezervare(rezervare);
                rezervari.remove(idRezervare);
                System.out.println("Rezervare anulată.");
            } else {
                System.out.println("Rezervarea nu a fost găsită.");
            }
        }

        public void modificaRezervare(int idRezervare, Date nouaDataPlecare, Aeroport nouDestinatie) {
            Rezervare rezervare = rezervari.get(idRezervare);
            if (rezervare != null) {
                Zbor zborVechi = rezervare.getZbor();
                if (zborVechi.getLocuriDisponibile() > 0) {
                    Zbor zborNou = new Zbor(zborVechi.getCodZbor(), zborVechi.getPlecare(), nouDestinatie, nouaDataPlecare, zborVechi.getLocuriDisponibile());
                    rezervare.getPasager().eliminaRezervare(rezervare);
                    rezervaZbor(rezervare.getPasager(), zborNou);
                    rezervari.put(rezervare.getId(), rezervare);
                    System.out.println("Rezervarea a fost modificată.");
                } else {
                    System.out.println("Nu sunt locuri disponibile pe noul zbor.");
                }
            } else {
                System.out.println("Rezervarea nu a fost găsită.");
            }
        }

        public void afisareDetaliiRezervare(int idRezervare) {
            Rezervare rezervare = rezervari.get(idRezervare);
            if (rezervare != null) {
                rezervare.getBilet().afisareDetalii();
                System.out.println("Bagaje:");
                for (Bagaj bagaj : rezervare.getBagaje()) {
                    System.out.println("Tip bagaj: " + bagaj.getTip() + ", Greutate: " + bagaj.getGreutate());
                }
            } else {
                System.out.println("Rezervarea nu a fost găsită.");
            }
        }

        public void adaugaBagajLaRezervare(int idRezervare, Bagaj bagaj) {
            Rezervare rezervare = rezervari.get(idRezervare);
            if (rezervare != null) {
                rezervare.adaugaBagaj(bagaj);
                System.out.println("Bagaj adăugat.");
            } else {
                System.out.println("Rezervarea nu a fost găsită.");
            }
        }

        public void checkInOnline(int idRezervare) {
            Rezervare rezervare = rezervari.get(idRezervare);
            if (rezervare != null) {
                System.out.println("Check-in complet pentru rezervarea ID " + idRezervare);
            } else {
                System.out.println("Rezervarea nu a fost găsită.");
            }
        }

        public void emiteBileteElectronice(int idRezervare) {
            Rezervare rezervare = rezervari.get(idRezervare);
            if (rezervare != null) {
                rezervare.getBilet().afisareDetalii();
                System.out.println("Bilet electronic emis.");
            } else {
                System.out.println("Rezervarea nu a fost găsită.");
            }
        }

        public void afisareIstoricRezervari(Pasager pasager) {
            List<Rezervare> rezervari = pasager.getRezervari();
            if (!rezervari.isEmpty()) {
                System.out.println("Istoric rezervări pentru " + pasager.getNume() + ":");
                for (Rezervare rezervare : rezervari) {
                    rezervare.getBilet().afisareDetalii();
                    System.out.println("ID rezervare: " + rezervare.getId());
                }
            } else {
                System.out.println("Nu există rezervări pentru acest pasager.");
            }
        }
    }

    public class Main {
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            ServiciuRezervare serviciuRezervare = new ServiciuRezervare();

            while (true) {
                System.out.println("\n--- Meniu Serviciu Rezervare ---");
                System.out.println("1. Creare aeroporturi");
                System.out.println("2. Creare zboruri");
                System.out.println("3. Creare pasageri");
                System.out.println("4. Cautare zboruri");
                System.out.println("5. Rezervare zbor");
                System.out.println("6. Afisare detalii rezervare");
                System.out.println("7. Adaugare bagaj");
                System.out.println("8. Check-in online");
                System.out.println("9. Emitere bilet electronic");
                System.out.println("10. Istoric rezervari");
                System.out.println("11. Modificare rezervare");
                System.out.println("12. Anulare rezervare");
                System.out.println("13. Iesire");
    
                System.out.print("Alege o optiune: ");
                int optiune = scanner.nextInt();
                scanner.nextLine(); // Consumă newline-ul rămas
    
                switch (optiune) {
                    case 1:
                        // Creare aeroporturi
                        System.out.print("Introduceti codul aeroportului: ");
                        String codAeroport = scanner.nextLine();
                        System.out.print("Introduceti numele aeroportului: ");
                        String numeAeroport = scanner.nextLine();
                        System.out.print("Introduceti orasul aeroportului: ");
                        String orasAeroport = scanner.nextLine();
                        Aeroport aeroport = new Aeroport(codAeroport, numeAeroport, orasAeroport);
                        serviciuRezervare.adaugaAeroport(aeroport);
                        // Adaugă aeroport la serviciul de rezervare (presupunem că există o metodă pentru asta)
                        System.out.println("Aeroport creat: " + aeroport);
                        break;
                    case 2:
 // Creare zboruri
                    System.out.print("Introduceti codul zborului: ");
                    String codZbor = scanner.nextLine();

                    // Afișare aeroporturi disponibile pentru selecție
                    System.out.println("Aeroporturi disponibile:");
                    List<Aeroport> aeroporturi = serviciuRezervare.getAeroporturi();
                    for (int i = 0; i < aeroporturi.size(); i++) {
                        System.out.println((i + 1) + ". " + aeroporturi.get(i).getCodIATA() + " - " + aeroporturi.get(i).getNume());
                    }

                    // Selecție aeroport de plecare
                    System.out.print("Selectati aeroportul de plecare (introduceti numarul): ");
                    int indexPlecare = scanner.nextInt() - 1;
                    Aeroport aeroportPlecare = aeroporturi.get(indexPlecare);

                    // Selecție aeroport de destinație
                    System.out.print("Selectati aeroportul de destinatie (introduceti numarul): ");
                    int indexDestinatie = scanner.nextInt() - 1;
                    Aeroport aeroportDestinatie = aeroporturi.get(indexDestinatie);

                    // Introducere data zborului
                    System.out.print("Introduceti data zborului (in format yyyy-MM-dd): ");
                    scanner.nextLine();  // Consumăm newline-ul rămas
                    String dataZbor = scanner.nextLine();
                    Date dataZborului;
                    try {
                        dataZborului = new SimpleDateFormat("yyyy-MM-dd").parse(dataZbor);
                    } catch (Exception e) {
                        System.out.println("Format invalid al datei. Zborul nu a fost creat.");
                        break;
                    }

                    // Introducere capacitate zbor
                    System.out.print("Introduceti capacitatea zborului: ");
                    int capacitate = scanner.nextInt();

                    // Creare și adăugare zbor
                    Zbor zborNou = new Zbor(codZbor, aeroportPlecare, aeroportDestinatie, dataZborului, capacitate);
                    serviciuRezervare.adaugaZbor(zborNou);
                    System.out.println("Zbor creat: " + codZbor);
                    break;
                    case 3:
                        // Creare pasageri
                        System.out.print("Introduceti id-ul pasagerului: ");
                        Integer idPasager = scanner.nextInt();
                        System.out.print("Introduceti numele pasagerului: ");
                        String numePasager = scanner.nextLine();
                        System.out.print("Introduceti email-ul pasagerului: ");
                        String emailPasager = scanner.nextLine();
                        Pasager pasager = new Pasager(idPasager, numePasager, emailPasager);
                        serviciuRezervare.adaugaPasager(pasager);
                        System.out.println("Pasager creat: " + pasager);
                        break;
                    case 4:
                        // Cautare zboruri
                        System.out.print("Introduceti numele aeroportului de plecare: ");
                        String codPlecare = scanner.nextLine();
                        System.out.print("Introduceti numele aeroportului de destinatie: ");
                        String codDestinatie = scanner.nextLine();
                        List<Zbor> zboruriGasite = serviciuRezervare.cautaZboruri(codPlecare, codDestinatie);
                        for (Zbor zbor : zboruriGasite) {
                            System.out.println("Zbor gasit: " + zbor.getCodZbor() + " - " + zbor.getDestinatie().getNume());
                        }
                        break;
                    case 5:
                        // Rezervare zbor
                        System.out.print("Introduceti ID-ul pasagerului: ");
                        int idPasagerul = scanner.nextInt();
                        System.out.print("Introduceti numele pasagerului: ");
                        int numePasagerul = scanner.nextInt();
                        System.out.print("Introduceti codul zborului: ");
                        String codZborRezervare = scanner.next();
                        // Presupunem că avem un pasager și zbor cu ID-ul și codul respective
                        serviciuRezervare.rezervaZbor(new Pasager(,"", ""), new Zbor(codZborRezervare, new Aeroport("", "", ""), new Aeroport("", "", ""), new Date(), 0));
                        System.out.println("Zbor rezervat.");
                        break;
                    case 6:
                        // Afisare detalii rezervare
                        System.out.print("Introduceti ID-ul rezervarii: ");
                        int idRezervare = scanner.nextInt();
                        serviciuRezervare.afisareDetaliiRezervare(idRezervare);
                        break;
                    case 7:
                        // Adaugare bagaj
                        System.out.print("Introduceti ID-ul rezervarii: ");
                        int idRezervareBagaj = scanner.nextInt();
                        System.out.print("Introduceti greutatea bagajului (kg): ");
                        double greutateBagaj = scanner.nextDouble();
                        scanner.nextLine(); // Consumă newline-ul rămas
                        System.out.print("Introduceti tipul bagajului (Check-in/Hand luggage): ");
                        String tipBagaj = scanner.nextLine();
                        Bagaj bagaj = new Bagaj(greutateBagaj, tipBagaj);
                        serviciuRezervare.adaugaBagajLaRezervare(idRezervareBagaj, bagaj);
                        System.out.println("Bagaj adaugat.");
                        break;
                    case 8:
                        // Check-in online
                        System.out.print("Introduceti ID-ul rezervarii: ");
                        int idRezervareCheckIn = scanner.nextInt();
                        serviciuRezervare.checkInOnline(idRezervareCheckIn);
                        System.out.println("Check-in efectuat.");
                        break;
                    case 9:
                        // Emitere bilet electronic
                        System.out.print("Introduceti ID-ul rezervarii: ");
                        int idRezervareBilet = scanner.nextInt();
                        serviciuRezervare.emiteBileteElectronice(idRezervareBilet);
                        System.out.println("Bilet electronic emis.");
                        break;
                    case 10:
                        // Istoric rezervări
                        System.out.print("Introduceti numele pasagerului: ");
                        String numePasagerIstoric = scanner.nextLine();
                        Pasager pasagerIstoric = new Pasager(numePasagerIstoric, "");
                        serviciuRezervare.afisareIstoricRezervari(pasagerIstoric);
                        break;
                    case 11:
                        // Modificare rezervare
                        System.out.print("Introduceti ID-ul rezervarii: ");
                        int idRezervareModificare = scanner.nextInt();
                        scanner.nextLine(); // Consumă newline-ul rămas
                        System.out.print("Introduceti noua data (in format yyyy-MM-dd): ");
                        String dataNoua = scanner.nextLine();
                        System.out.print("Introduceti codul noului aeroport de destinatie: ");
                        String codDestinatieNou = scanner.nextLine();
                        // Adăugați logica de modificare a rezervării
                        System.out.println("Rezervare modificata.");
                        break;
                    case 12:
                        // Anulare rezervare
                        System.out.print("Introduceti ID-ul rezervarii: ");
                        int idRezervareAnulare = scanner.nextInt();
                        serviciuRezervare.anuleazaRezervare(idRezervareAnulare);
                        System.out.println("Rezervare anulata.");
                        break;
                    case 13:
                        // Iesire
                        System.out.println("La revedere!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Optiune invalida, va rugam incercati din nou.");
                        break;
                }
            }
        }
    }