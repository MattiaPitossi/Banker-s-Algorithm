package banker;
import java.util.Scanner;

public class Banker {

    public static void main (String [] arg){

        Scanner scanner = new Scanner(System.in);

        System.out.println("Inserisci il numero di processi: ");
        int n = scanner.nextInt();

        System.out.println("Inserisci il numero delle risorse: ");
        int m = scanner.nextInt();

        int risorseDisponibili[] = new int[m];
        int risorseAllocate[][] = new int[n][m];
        int risorseMassime[][] = new int[n][m];
        int risorseResidue[][] = new int[n][m];
        int richiesta[] = new int[m];
        
        //array risorse disponibili
        for(int i=0; i<m; i++){
            System.out.println("Inserisci le risorse disponibili (una per volta): ");
            risorseDisponibili[i] = scanner.nextInt();
        }
  
        //matrice risorse assegnate
        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++){
                System.out.println("Inserisci le istanze della risorsa " + (j+1) + " del processo " + (i+1) + ": ");
                risorseAllocate[i][j] = scanner.nextInt();
            }
        }
    
        //matrice risorse massime
        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++){
                System.out.println("Inserisci le istanze massime della risorsa " + (j+1) + " del processo " + (i+1) + ": ");
                risorseMassime[i][j] = scanner.nextInt();
                while(risorseMassime[i][j]<risorseAllocate[i][j]){
                    System.out.println("attenzione: la richiesta massima deve essere maggiore o uguale a " + risorseAllocate[i][j]);
                    System.out.println("Inserisci le istanze massime della risorsa " + (j+1) + " del processo " + (i+1) + ": ");
                    risorseMassime[i][j] = scanner.nextInt();
                }
            }
        }

        //calcolo matrice risorse residue
        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++){
                risorseResidue[i][j] = risorseMassime[i][j] - risorseAllocate[i][j];
            }
        }

        if(controllaStato(risorseDisponibili, risorseAllocate, risorseResidue, n, m)){
            System.out.println("Il sistema è in stato sicuro");
            System.out.println("Scegli un processo per cui fare richiesta: ");
            int np = scanner.nextInt();
            for(int j=0; j<m; j++){
                System.out.println("Inserisci il numero di istanze della risorsa " + (j+1) + ": ");
                richiesta[j] = scanner.nextInt();
            }
            if(controllaRichiesta(richiesta, risorseDisponibili, risorseResidue, m, np-1)){
                
                for(int j=0; j<m;j++){
                        risorseDisponibili[j] -= richiesta[j];
                        risorseAllocate[np-1][j] += richiesta[j];
                        risorseResidue[np-1][j] -= richiesta[j];
                }
                if(controllaStato(risorseDisponibili, risorseAllocate, risorseResidue, n, m)){
                    //stampa matrice allocata attuale   
                    for(int i=0; i<n; i++){
                        for(int j=0; j<m; j++){
                            System.out.print(risorseAllocate[i][j] + " ");
                        }
                        System.out.println("");
                    }
                } else {
                    System.out.println("La richiesta effettuata porta a uno stato non sicuro");
                }
            } else {
                System.out.println("Non è possibile effettuare quella richiesta al momento");
            } 
               
            
        } else {
            System.out.println("Il sistema non è in stato sicuro");
        }
        scanner.close();
    }

    //controlla se la matrice si trova in stato sicuro
    public static boolean controllaStato(int disponibile[], int risorseAllocate[][], int risorseRichieste[][], int np, int nr){
        int n = np;
        int m = nr;
        int work[] = new int[m];
        boolean finish[] = new boolean[n];
        
        for(int i=0; i<m; i++){
            work[i]=disponibile[i];
        }   

        for(int i=0; i<n; i++){
            finish[i] = false;
        }

        int check = 0;
        int totTrueCounter = 0;
        int totFalseCounter = 0;
        boolean termina = false;
        int grant = 0;
        boolean ok = false;
        do {
            check = n - totTrueCounter;
            totFalseCounter = 0;        
            ok = false;
            for(int i=0; i<n; i++){
                grant = 0;
                if(!finish[i]){
                    for(int j=0; j<m; j++){
                        if(work[j] >= risorseRichieste[i][j]){
                            grant++;
                        }
                    }
                    if(grant == m){
                        ok = true;
                    }
                }
                if(!finish[i] && ok){
                    totTrueCounter ++;
                    finish[i] = true;
                    for(int j=0; j<m; j++){
                        work[j] += risorseAllocate[i][j];
                    }
                    break;
                } else if (!finish[i]){
                    totFalseCounter ++;
                }
            }

            if(totTrueCounter == n || check == totFalseCounter){
                termina = true;
            }
            
        } while(!termina);
        
        int count2 = 0;
        
        for(int j=0; j<n; j++){
            if(finish[j] == true){
                count2 ++;
            }
        }
        if(count2 == n){
            return true;
        } else {
            return false;
        }
    }

    //verifica lo stato futuro della matrice in base alla richiesta
    public static boolean controllaRichiesta(int richiesta [], int disponibile[], int risorseResidue[][], int m, int numeroProcesso){
        int counter = 0;
        for(int j=0; j<m; j++){
            if(risorseResidue[numeroProcesso][j]<richiesta[j]){
                counter++;
            }
        }
        if(counter == m){
            return false;
        }
        counter = 0;
        for(int j=0; j<m; j++){
            if(disponibile[j]<richiesta[j]){
                counter ++;
            }
        }
        if(counter == m){
            return false;
        }
        return true;
    }
}