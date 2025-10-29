package CreditCalculator;

public class CreditCalculator {
    public static void main(String[] args) {
        CalculatorLogic calculatorLogic = new CalculatorLogic();

        String type = System.getProperty("type");
        String principal = System.getProperty("principal");
        String periods = System.getProperty("periods");
        String interest = System.getProperty("interest");
        String payment = System.getProperty("payment");

        calculatorLogic.dataAnalyze(type, principal, periods, interest, payment);
    }
}

// ---- Клас для аналізу параметрів ----
class CalculatorLogic {

    public void dataAnalyze(String type, String principal, String periods, String interest, String payment) {
        if (type == null || (!type.equals("diff") && !type.equals("annuity")) || interest == null) {
            System.out.println("Incorrect parameters");
            return;
        }

        double P = principal != null ? Double.parseDouble(principal) : 0;
        double A = payment != null ? Double.parseDouble(payment) : 0;
        int n = periods != null ? Integer.parseInt(periods) : 0;
        double i = Double.parseDouble(interest) / (12 * 100);

        // Перевірка від’ємних значень
        if (P < 0 || A < 0 || n < 0 || i < 0) {
            System.out.println("Incorrect parameters");
            return;
        }

        // Вибір типу кредиту
        switch (type) {
            case "diff" -> DiffCalculator.calculateDifferentiated(P, n, i, A);
            case "annuity" -> AnnuityCalculator.calculateAnnuity(P, A, n, i);
        }
    }
}

// ---- Клас для диференційованих платежів ----
class DiffCalculator {

    public static void calculateDifferentiated(double P, int n, double i, double A) {
        // Перевірка: диференційований платіж не може мати фіксовану суму A
        if (A != 0 || P == 0 || n == 0 || i == 0) {
            System.out.println("Incorrect parameters");
            return;
        }

        double totalPayment = 0;
        for (int m = 1; m <= n; m++) {
            double Dm = Math.ceil((P / n) + i * (P - (P * (m - 1) / n)));
            totalPayment += Dm;
            System.out.println("Month " + m + ": payment is " + (int) Dm);
        }

        System.out.println("Overpayment = " + (int) (totalPayment - P));
    }
}

// ---- Клас для ануїтетних платежів ----
class AnnuityCalculator {

    public static void calculateAnnuity(double P, double A, int n, double i) {
        if (i == 0) {
            System.out.println("Incorrect parameters");
            return;
        }

        if (P > 0 && n > 0 && A == 0) {
            // Розрахунок щомісячного платежу
            double pow = Math.pow(1 + i, n);
            double annuityPayment = Math.ceil(P * (i * pow) / (pow - 1));
            System.out.println("Your annuity payment = " + (int) annuityPayment + "!");
            System.out.println("Overpayment = " + (int) (annuityPayment * n - P));

        } else if (P > 0 && A > 0 && n == 0) {
            // Розрахунок кількості місяців
            double months = Math.log(A / (A - i * P)) / Math.log(1 + i);
            int monthsRounded = (int) Math.ceil(months);
            int years = monthsRounded / 12;
            int remainingMonths = monthsRounded % 12;

            StringBuilder result = new StringBuilder("It will take ");
            if (years > 0) result.append(years).append(" year").append(years > 1 ? "s" : "");
            if (years > 0 && remainingMonths > 0) result.append(" and ");
            if (remainingMonths > 0) result.append(remainingMonths).append(" month").append(remainingMonths > 1 ? "s" : "");
            result.append(" to repay this loan!");

            System.out.println(result);

            // Переплата рахується через точне число місяців
            double exactOverpayment = A * months - P;
            System.out.println("Overpayment = " + (int) Math.round(exactOverpayment));

        } else if (P == 0 && A > 0 && n > 0) {
            // Розрахунок основної суми кредиту
            double pow = Math.pow(1 + i, n);
            double loanPrincipal = Math.floor(A / ((i * pow) / (pow - 1)));
            System.out.println("Your loan principal = " + (int) loanPrincipal + "!");
            System.out.println("Overpayment = " + (int) (A * n - loanPrincipal));

        } else {
            System.out.println("Incorrect parameters");
        }
    }
}
