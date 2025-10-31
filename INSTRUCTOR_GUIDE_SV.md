# 👨‍🏫 Instruktörsguide: AWS Distributions-Tutorial (Svenska)

> **Snabbreferens för instruktörer som undervisar AWS-distribution**

## 🎯 Lärandemål

Studenter kommer att kunna:

- [ ] Distribuera React-applikationer till AWS Amplify
- [ ] Distribuera Spring Boot-applikationer till AWS Elastic Beanstalk
- [ ] Konfigurera AWS RDS PostgreSQL-databaser
- [ ] Sätta upp CI/CD-pipelines med GitHub Actions
- [ ] Implementera säkerhetsrutiner enligt bästa praxis
- [ ] Övervaka och felsöka molnapplikationer
- [ ] Hantera AWS-kostnader och resurser

## ⏱️ Tidsallokering (4-timmars session)

| Fas         | Varaktighet | Aktivitet                               |
| ----------- | ----------- | --------------------------------------- |
| **Fas 1**   | 30 min      | Lokal setup och verifiering             |
| **Fas 2**   | 45 min      | AWS infrastrukturuppsättning            |
| **Fas 3**   | 20 min      | Miljökonfiguration                      |
| **Fas 4**   | 45 min      | Applikationsdistribution                |
| **Fas 5**   | 30 min      | CI/CD pipeline-uppsättning              |
| **Fas 6**   | 20 min      | Testning och verifiering                |
| **Fas 7**   | 30 min      | Felsökningsövning                       |
| **Fas 8**   | 10 min      | Kostnadshanteringsgenomgång            |
| **Fas 9**   | 10 min      | Sammanfattning och nästa steg          |

## 📋 Förberedelsechecklista

### För instruktörer:

- [ ] AWS-konto med instruktörsåtkomst
- [ ] Demo-miljö förinstallerad
- [ ] Backup GitHub-repository redo
- [ ] Kostnadsvarningar konfigurerade
- [ ] Skärmdelning testad

### För studenter:

- [ ] Verifiera AWS-kontoupprettelse (gratis nivå)
- [ ] GitHub-kontobekräftelse
- [ ] Checklista för programvaruinstallation
- [ ] Dela repositoryåtkomst

## 🚨 Vanliga studentproblem

### Problem 1: AWS-kontoproblem

**Symptom:** Kan inte skapa AWS-konto, faktureringsproblem
**Lösningar:**

- Vägleda genom kontoverifiering
- Förklara begränsningar för gratis nivå
- Hjälpa med kreditkortsverifiering

### Problem 2: Behörighetsfel

**Symptom:** IAM-behörighet nekad, kan inte skapa resurser
**Lösningar:**

- Kontrollera AWS-kontotyp (root vs IAM-användare)
- Verifiera regionval
- Vägleda genom IAM-policykoppling

### Problem 3: Lokala miljöproblem

**Symptom:** Java/Node-versionskonflikter, PostgreSQL körs inte
**Lösningar:**

- Använd tillhandahållen Docker-setup om tillgänglig
- Vägleda genom versionshanterare (sdkman, nvm)
- Alternativ: Molnutvecklingsmiljöer

### Problem 4: Nätverk/brandväggsproblem

**Symptom:** Kan inte komma åt AWS-tjänster, anslutning tar för lång tid
**Lösningar:**

- Kontrollera företagsbrandväggsinställningar
- Verifiera VPN-konfigurationer
- Använd AWS CloudShell som alternativ

## 🛠️ Demo-kontrollpunkter

### Kontrollpunkt 1: Lokal miljö (Fas 1)

**Verifiera:**

```bash
curl http://localhost:8080/api/health
curl http://localhost:5173
```

**Förväntat:** Båda tjänsterna svarar korrekt

### Kontrollpunkt 2: AWS-infrastruktur (Fas 2)

**Verifiera:**

- RDS-instansstatus: "Available"
- Elastic Beanstalk-miljö: "Ok"
- Amplify-app: Skapad framgångsrikt

### Kontrollpunkt 3: Backend-distribution (Fas 4)

**Verifiera:**

```bash
curl http://eb-env-url.elasticbeanstalk.com/api/health
```

**Förväntat:** "Customer Management API is running!"

### Kontrollpunkt 4: Frontend-distribution (Fas 4)

**Verifiera:**

- Amplify URL laddar React-app
- Kan interagera med backend API
- CORS fungerar korrekt

### Kontrollpunkt 5: CI/CD Pipeline (Fas 5)

**Verifiera:**

- GitHub Actions körs framgångsrikt
- Automatiserad distribution fungerar
- Korrekt hemlig konfiguration

## 💡 Undervisningstips

### Effektiva strategier:

1. **Börja med slutmålet** - Visa färdig applikation först
2. **Använd parprogrammering** - Studenter hjälper varandra
3. **Regelbundna kontrollpunkter** - Låt ingen halka efter
4. **Screenshota allt** - AWS Console ändras ofta
5. **Förbered dig för misslyckanden** - Använd fel som lärtillfällen

### Vanliga fallgropar att undvika:

- **Stressa igenom AWS-setup** - Studenter behöver tid att förstå
- **Ignorera kostnadspåverkan** - Betona alltid städning
- **Inte testa lokalt först** - Lokal miljö måste fungera
- **Hoppa över säkerhetsdiskussioner** - Förklara varför säkerhet spelar roll

## 🔧 Felsökningsskript

### Snabb hälsokontroll:

```bash
#!/bin/bash
echo "=== Miljö hälsokontroll ==="
echo "Java Version: $(java -version 2>&1 | head -1)"
echo "Node Version: $(node -version)"
echo "AWS CLI: $(aws --version)"
echo "Git: $(git --version)"
echo "PostgreSQL: $(psql --version)"

echo "=== Tjänststatus ==="
curl -I http://localhost:8080/api/health 2>/dev/null && echo "Backend: OK" || echo "Backend: MISSLYCKADES"
curl -I http://localhost:5173 2>/dev/null && echo "Frontend: OK" || echo "Frontend: MISSLYCKADES"
```

### AWS-resursrensning:

```bash
#!/bin/bash
echo "=== Rensar upp AWS-resurser ==="
aws elasticbeanstalk terminate-environment --environment-name customer-management-api-env
aws rds delete-db-instance --db-instance-identifier customer-db --skip-final-snapshot
aws amplify delete-app --app-id $AMPLIFY_APP_ID
echo "Rensning initierad - kontrollera AWS Console för slutförande"
```

## 📊 Bedömningsrubrik

### Distributionsframgång (40 poäng)

- [ ] Lokal miljö fungerar (10 p)
- [ ] AWS-infrastruktur skapad (10 p)
- [ ] Backend distribuerad framgångsrikt (10 p)
- [ ] Frontend distribuerad framgångsrikt (10 p)

### Konfiguration & säkerhet (30 poäng)

- [ ] Miljövariabler konfigurerade (10 p)
- [ ] Säkerhetsgrupper korrekt inställda (10 p)
- [ ] CORS-konfiguration fungerar (10 p)

### CI/CD-implementering (20 poäng)

- [ ] GitHub Actions arbetsflöde kör (10 p)
- [ ] Automatiserad distribution fungerar (10 p)

### Dokumentation & rensning (10 poäng)

- [ ] Korrekt dokumentation av process (5 p)
- [ ] Resurser städade ordentligt (5 p)

## 🎓 Fördjupningsaktiviteter

### För avancerade studenter:

1. **Lägg till autentisering:** Implementera JWT med AWS Cognito
2. **Prestandaövervakning:** Sätt upp CloudWatch-dashboards
3. **Blue-Green distribution:** Implementera nolltid-distributioner
4. **Infrastructure as Code:** Konvertera setup till CloudFormation/CDK
5. **Säkerhetshärdning:** Implementera ytterligare säkerhetsåtgärder

### För studenter som har svårigheter:

1. **Förenklad version:** Distribuera endast frontend eller backend
2. **Väglett läge:** Ge mer detaljerade steg-för-steg-instruktioner
3. **Para ihop med mentor:** Tilldela avancerade studenter som hjälpare
4. **Alternativa plattformar:** Använd enklare distributionsalternativ

## 📚 Ytterligare resurser

### För instruktörer:

- [AWS Educate Program](https://aws.amazon.com/education/awseducate/)
- [AWS Academy Curriculum](https://aws.amazon.com/training/awsacademy/)
- [Spring Boot AWS Workshop](https://spring.io/guides/gs/spring-boot-docker/)

### För studenter:

- [AWS Free Tier Guide](https://aws.amazon.com/free/)
- [12-Factor App Methodology](https://12factor.net/)
- [Cloud Native Patterns](https://docs.microsoft.com/en-us/azure/architecture/patterns/)

## 💰 Kostnadshantering för klasser

### Rekommenderat tillvägagångssätt:

1. **Använd AWS Educate Credits** när tillgängligt
2. **Sätt strikta tidsgränser** för resursanvändning
3. **Automatiserade rensningsskript** körs efter varje session
4. **Övervaka kostnader dagligen** under undervisningsperioden
5. **Ha backup-plan** om kostnaderna överstiger budget

### Typiska kostnader (per student):

- **Under tutorial**: $1-3/dag
- **Om lämnad igång**: $30-50/månad
- **Med korrekt rensning**: $0-1/månad

---

## 🎯 Sessionsagenda-mall

### Öppning (15 minuter)

- [ ] Välkommen och presentationer
- [ ] Genomgång av lärandemål
- [ ] Verifiering av förkunskaper
- [ ] AWS-kontobekräftelse

### Fas 1: Lokal setup (30 minuter)

- [ ] Klona repository
- [ ] Kör setup-skript
- [ ] Verifiera lokal miljö
- [ ] Felsök problem

### Fas 2: AWS-infrastruktur (45 minuter)

- [ ] AWS Console-överblick
- [ ] Skapa RDS-databas
- [ ] Sätt upp Elastic Beanstalk
- [ ] Skapa Amplify-app

### Paus (15 minuter)

### Fas 3-4: Distribution (65 minuter)

- [ ] Konfigurera miljövariabler
- [ ] Distribuera backend-applikation
- [ ] Distribuera frontend-applikation
- [ ] Testa end-to-end-funktionalitet

### Fas 5: CI/CD (30 minuter)

- [ ] GitHub Actions-överblick
- [ ] Konfigurera hemligheter
- [ ] Testa automatiserad distribution

### Fas 6-7: Testning & felsökning (50 minuter)

- [ ] End-to-end-testning
- [ ] Lösning av vanliga problem
- [ ] Övervakning och loggning

### Avslutning (10 minuter)

- [ ] Genomgång av kostnadshantering
- [ ] Rensningsinstruktioner
- [ ] Nästa steg och resurser
- [ ] Frågor och svar

---

**📋 Kom ihåg:** Målet är inte bara distribution, utan att förstå molnnativa applikationsutvecklingsmönster som studenter kommer att använda i sina karriärer.