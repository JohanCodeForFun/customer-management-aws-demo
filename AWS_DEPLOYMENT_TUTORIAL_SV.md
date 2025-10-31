# 🚀 AWS Deployment Tutorial: Full-Stack Customer Management Application (Svenska)

> **Komplett guide för att distribuera en Spring Boot + React applikation till AWS**

## 📚 Vad du kommer att lära dig

Genom att följa denna tutorial kommer du att:
- Distribuera en React frontend till AWS Amplify
- Distribuera en Spring Boot backend till AWS Elastic Beanstalk
- Sätta upp en PostgreSQL databas på AWS RDS
- Konfigurera CI/CD med GitHub Actions
- Implementera säkerhetsrutiner enligt bästa praxis
- Övervaka och felsöka din distribuerade applikation

## 🎯 Förkunskaper

### Nödvändiga kunskaper
- Grundläggande förståelse för Spring Boot och React
- Bekantskap med Git och GitHub
- Grundläggande kommandoradsanvändning
- Förståelse för REST API:er

### Nödvändiga konton
- [ ] **AWS-konto** (med fakturering aktiverad)
- [ ] **GitHub-konto** 
- [ ] **Domän** (valfritt, för anpassade URL:er)

### Nödvändig programvara
- [ ] **Java 17+** - `java -version`
- [ ] **Node.js 18+** - `node -version`
- [ ] **Git** - `git --version`
- [ ] **AWS CLI** - `aws --version`
- [ ] **PostgreSQL** (för lokal utveckling) - `psql --version`

## 📁 Projektöversikt

Vår applikation består av:
```
customer-management-aws-demo/
├── client/          # React TypeScript frontend
├── server/          # Spring Boot backend API
├── .github/         # CI/CD arbetsflöden
└── docs/           # Dokumentation
```

**Arkitektur:**
- **Frontend**: React → AWS Amplify (Global CDN)
- **Backend**: Spring Boot → AWS Elastic Beanstalk (Auto-skalning)
- **Databas**: PostgreSQL → AWS RDS (Hanterad databas)
- **CI/CD**: GitHub Actions → Automatiserad distribution

---

## 🏁 Fas 1: Lokal utvecklingsmiljö

### Steg 1.1: Klona och sätt upp projektet

```bash
# Klona repositoryt
git clone https://github.com/JohanCodeForFun/customer-management-aws-demo.git
cd customer-management-aws-demo

# Gör setup-skriptet körbart och kör det
chmod +x setup.sh
./setup.sh
```

### Steg 1.2: Verifiera lokal installation

**Starta Backend:**
```bash
cd server
./mvnw spring-boot:run
```

**Starta Frontend (ny terminal):**
```bash
cd client
npm run dev
```

**Testa applikationen:**
- Frontend: http://localhost:5173
- Backend API: http://localhost:8080/api/customers
- Hälsokontroll: http://localhost:8080/api/health

✅ **Kontrollpunkt:** Du bör se kundhanteringsgränssnittet och kunna lägga till/ta bort kunder.

---

## ☁️ Fas 2: AWS infrastruktur

### Steg 2.1: Installera och konfigurera AWS CLI

```bash
# Installera AWS CLI (macOS)
brew install awscli

# Konfigurera med dina referenser
aws configure
```

**Ange när du tillfrågas:**
- AWS Access Key ID: `[Din access key]`
- AWS Secret Access Key: `[Din secret key]`
- Default region: `us-east-1` (eller din föredragna region)
- Default output format: `json`

### Steg 2.2: Skapa AWS RDS databas

**Navigera till AWS Console → RDS:**

1. **Klicka "Create database"**
2. **Engine options:**
   - Engine type: `PostgreSQL`
   - Version: `PostgreSQL 14.x` (senaste)

3. **Templates:**
   - Välj `Free tier` (för lärande)

4. **Settings:**
   - DB instance identifier: `customer-db`
   - Master username: `customeruser`
   - Master password: `DittSäkraLösenord123!`

5. **Instance configuration:**
   - DB instance class: `db.t3.micro`

6. **Storage:**
   - Allocated storage: `20 GB`
   - Storage type: `General Purpose SSD`

7. **Connectivity:**
   - VPC: `Default VPC`
   - Subnet group: `default`
   - Public access: `Yes` (för lärande - använd No i produktion)
   - VPC security group: `Create new`
   - Security group name: `customer-db-sg`

8. **Database authentication:**
   - Database authentication: `Password authentication`

9. **Additional configuration:**
   - Initial database name: `customerdb`

10. **Klicka "Create database"**

⏱️ **Vänta 5-10 minuter** för att databasen ska skapas.

**Notera RDS endpoint** (t.ex. `customer-db.xxxxxxxxxx.us-east-1.rds.amazonaws.com`)

### Steg 2.3: Skapa Elastic Beanstalk applikation

**Navigera till AWS Console → Elastic Beanstalk:**

1. **Klicka "Create application"**
2. **Application information:**
   - Application name: `customer-management-api`
   - Description: `Spring Boot Customer Management API`

3. **Platform:**
   - Platform: `Java`
   - Platform branch: `Corretto 17`
   - Platform version: `(senaste)`

4. **Application code:**
   - Sample application: `Sample application`

5. **Klicka "Create application"**

⏱️ **Vänta 5-10 minuter** för att miljön ska skapas.

### Steg 2.4: Skapa AWS Amplify app

**Navigera till AWS Console → AWS Amplify:**

1. **Klicka "New app" → "Host web app"**
2. **Välj "GitHub"**
3. **Auktorisera AWS Amplify** att komma åt ditt GitHub-konto
4. **Välj repository:** `customer-management-aws-demo`
5. **Välj branch:** `main`
6. **App name:** `customer-management-frontend`
7. **Build settings:** AWS kommer att upptäcka React-appen automatiskt
8. **Advanced settings:**
   - Build specification: Använd befintlig `amplify.yml`
9. **Klicka "Save and deploy"**

⏱️ **Vänta 3-5 minuter** för den första byggnationen.

---

## 🔧 Fas 3: Miljökonfiguration

### Steg 3.1: Konfigurera miljövariabler

**Elastic Beanstalk miljövariabler:**

1. Gå till **Elastic Beanstalk Console** → `customer-management-api-env`
2. **Configuration** → **Software** → **Edit**
3. **Lägg till Environment properties:**

```
RDS_HOSTNAME=customer-db.xxxxxxxxxx.us-east-1.rds.amazonaws.com
RDS_PORT=5432
RDS_DB_NAME=customerdb
RDS_USERNAME=customeruser
RDS_PASSWORD=DittSäkraLösenord123!
CORS_ALLOWED_ORIGINS=https://main.d1234567890123.amplifyapp.com
```

4. **Klicka "Apply"**

**Amplify miljövariabler:**

1. Gå till **Amplify Console** → `customer-management-frontend`
2. **Environment variables** → **Manage variables**
3. **Lägg till variabel:**

```
VITE_API_BASE_URL=http://customer-management-api-env.xxxxxxxxxx.us-east-1.elasticbeanstalk.com/api
```

4. **Spara**

### Steg 3.2: Uppdatera säkerhetsgrupper

**RDS säkerhetsgrupp:**

1. **EC2 Console** → **Security Groups**
2. **Hitta:** `customer-db-sg`
3. **Edit inbound rules** → **Add rule:**
   - Type: `PostgreSQL`
   - Port: `5432`
   - Source: `Custom` → Välj Elastic Beanstalk säkerhetsgrupp
4. **Save rules**

---

## 🚀 Fas 4: Applikationsdistribution

### Steg 4.1: Förbered backend för distribution

**Uppdatera application-production.properties:**
```bash
cd server/src/main/resources
```

Verifiera att filen innehåller:
```properties
spring.datasource.url=jdbc:postgresql://${RDS_HOSTNAME:localhost}:${RDS_PORT:5432}/${RDS_DB_NAME:customerdb}
spring.datasource.username=${RDS_USERNAME:customeruser}
spring.datasource.password=${RDS_PASSWORD:customerpass}
cors.allowed-origins=${CORS_ALLOWED_ORIGINS:http://localhost:5173}
```

### Steg 4.2: Bygg och distribuera backend

**Skapa distributionspaket:**
```bash
cd server
./mvnw clean package -DskipTests
```

**Distribuera till Elastic Beanstalk:**
1. **Elastic Beanstalk Console** → `customer-management-api-env`
2. **Upload and deploy**
3. **Choose file:** `server/target/relational-data-access-0.0.1-SNAPSHOT.jar`
4. **Version label:** `v1.0.0`
5. **Deploy**

⏱️ **Vänta 3-5 minuter** för distribution.

### Steg 4.3: Testa backend-distribution

```bash
# Ersätt med din faktiska Elastic Beanstalk URL
curl http://customer-management-api-env.xxxxxxxxxx.us-east-1.elasticbeanstalk.com/api/health

# Bör returnera: "Customer Management API is running!"
```

### Steg 4.4: Distribuera frontend

**Utlös Amplify byggnation:**
1. **Amplify Console** → `customer-management-frontend`
2. **Main branch** → **Redeploy this version**

Eller pusha en ändring för att utlösa automatisk distribution:
```bash
git add .
git commit -m "Deploy to AWS"
git push origin main
```

---

## 🔒 Fas 5: Sätt upp CI/CD pipeline

### Steg 5.1: Konfigurera GitHub hemligheter

**GitHub Repository → Settings → Secrets and variables → Actions:**

Lägg till dessa hemligheter:
```
AWS_ACCESS_KEY_ID=din_access_key_här
AWS_SECRET_ACCESS_KEY=din_secret_key_här
VITE_API_BASE_URL=http://din-eb-url.elasticbeanstalk.com/api
AMPLIFY_APP_ID=ditt_amplify_app_id_här
```

### Steg 5.2: Uppdatera CI/CD arbetsflöde

Repositoryt innehåller redan:
- `.github/workflows/continuous-integration-build.yml` - För testning
- `.github/workflows/deploy.yml` - För distribution

**Uppdatera deploy.yml med dina värden:**
```yaml
env:
  AWS_REGION: us-east-1  # Din region
  EB_APPLICATION_NAME: customer-management-api
  EB_ENVIRONMENT_NAME: customer-management-api-env
```

### Steg 5.3: Testa CI/CD pipeline

```bash
# Gör en liten ändring
echo "# Test deployment" >> README.md

# Commit och pusha
git add .
git commit -m "Test CI/CD pipeline"
git push origin main
```

**Övervaka i GitHub:**
- **Actions tab** → Titta på bygg- och distributionsprocessen

---

## 🧪 Fas 6: Testning och verifiering

### Steg 6.1: End-to-end testning

**Testa det kompletta flödet:**

1. **Öppna din Amplify URL** (t.ex. `https://main.d1234567890123.amplifyapp.com`)
2. **Verifiera att frontend laddas**
3. **Testa kundoperationer:**
   - Lägg till en ny kund
   - Visa kundlista
   - Ta bort en kund
   - Sök efter kunder

### Steg 6.2: API-testning

```bash
# Hälsokontroll
curl https://din-amplify-url.amplifyapp.com

# API hälsokontroll
curl http://din-eb-url.elasticbeanstalk.com/api/health

# Hämta kunder
curl http://din-eb-url.elasticbeanstalk.com/api/customers

# Lägg till kund
curl -X POST http://din-eb-url.elasticbeanstalk.com/api/customers \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Johan","lastName":"Andersson"}'
```

### Steg 6.3: Övervaka applikationer

**CloudWatch loggar:**
1. **CloudWatch Console** → **Log groups**
2. **Hitta:** `/aws/elasticbeanstalk/customer-management-api-env/var/log/eb-docker/containers/eb-current-app`
3. **Visa loggar** för fel och övervakning

**Amplify byggloggar:**
1. **Amplify Console** → **Build history**
2. **Visa byggdetaljer** för eventuella problem

---

## 🔧 Fas 7: Felsökningsguide

### Vanliga problem och lösningar

#### **Problem 1: Databasanslutning misslyckades**
```
Error: Connection to localhost:5432 refused
```

**Lösning:**
- Verifiera RDS endpoint i miljövariabler
- Kontrollera att säkerhetsgruppen tillåter anslutningar från EB
- Verifiera databasreferenser

#### **Problem 2: CORS-fel i webbläsare**
```
Access to fetch at 'http://api..' blocked by CORS policy
```

**Lösning:**
- Uppdatera `CORS_ALLOWED_ORIGINS` i EB miljövariabler
- Inkludera din Amplify-domän
- Distribuera om backend

#### **Problem 3: Frontend-byggnation misslyckas**
```
Error: VITE_API_BASE_URL is not defined
```

**Lösning:**
- Kontrollera Amplify miljövariabler
- Verifiera att API URL:en är korrekt
- Utlös byggnation igen

#### **Problem 4: GitHub Actions misslyckas**
```
Error: AWS credentials not found
```

**Lösning:**
- Verifiera att GitHub hemligheter är korrekta
- Kontrollera IAM-behörigheter för access keys
- Säkerställ att hemliga namnen matchar arbetsflödesfilen

### Hälsokontrollskommandon

```bash
# Kontrollera om tjänster körs
curl -I http://din-eb-url.elasticbeanstalk.com/api/health
curl -I https://din-amplify-url.amplifyapp.com

# Kontrollera databasanslutning
psql -h din-rds-endpoint.rds.amazonaws.com -U customeruser -d customerdb

# Kontrollera loggar
aws logs describe-log-groups --region us-east-1
```

---

## 💰 Fas 8: Kostnadshantering

### Förväntade månadskostnader (Free Tier)

- **RDS (db.t3.micro)**: $0 (12 månader gratis)
- **Elastic Beanstalk**: $0 (plattformen är gratis, betala för EC2)
- **EC2 (t3.micro)**: $0 (12 månader gratis, 750 timmar/månad)
- **Amplify**: $0 (1GB lagring + 15GB bandbredd gratis månadsvis)
- **Totalt**: ~$0-5/månad (beroende på användning)

### Tips för kostnadsoptimering

1. **Stoppa miljöer när de inte används:**
   ```bash
   # Avsluta EB miljö
   aws elasticbeanstalk terminate-environment --environment-name customer-management-api-env
   ```

2. **Använd RDS snapshots** istället för körande instanser
3. **Övervaka användning** i AWS Billing Dashboard
4. **Sätt upp fakturavarningar** för oväntade avgifter

---

## 🎓 Fas 9: Nästa steg och lärande

### Vad du har åstadkommit

✅ **Distribuerat en fullstack-applikation till AWS**  
✅ **Satt upp en produktionsdatabas**  
✅ **Implementerat CI/CD pipeline**  
✅ **Konfigurerat övervakning och loggning**  
✅ **Tillämpat säkerhetsrutiner enligt bästa praxis**  

### Fortsatt lärande

1. **Säkerhetsförbättringar:**
   - Sätt upp SSL-certifikat
   - Implementera JWT-autentisering
   - Lägg till API hastighetsbegränsning

2. **Prestandaoptimering:**
   - Lägg till CloudFront CDN
   - Implementera caching-strategier
   - Databasoptimering

3. **Övervakning och varningar:**
   - Sätt upp CloudWatch alarm
   - Implementera applikationsövervakning
   - Lägg till loggningsdashboards

4. **Infrastructure as Code:**
   - Lär dig AWS CloudFormation
   - Prova AWS CDK
   - Utforska Terraform

### Ytterligare resurser

- **AWS Dokumentation**: [aws.amazon.com/documentation/](https://aws.amazon.com/documentation/)
- **Spring Boot på AWS**: [spring.io/guides/gs/spring-boot-docker/](https://spring.io/guides/gs/spring-boot-docker/)
- **React Distribution**: [create-react-app.dev/docs/deployment/](https://create-react-app.dev/docs/deployment/)

---

## 🚨 Rensningsinstruktioner

**När du är klar med lärandet, rensa upp för att undvika avgifter:**

```bash
# Ta bort Amplify app
aws amplify delete-app --app-id ditt-app-id

# Avsluta Elastic Beanstalk
aws elasticbeanstalk terminate-environment --environment-name customer-management-api-env

# Ta bort RDS instans
aws rds delete-db-instance --db-instance-identifier customer-db --skip-final-snapshot

# Ta bort säkerhetsgrupper och andra resurser genom AWS Console
```

---

## 📞 Få hjälp

- **Felsökningsguide**: Se `TROUBLESHOOTING_SV.md`
- **Säkerhetsinformation**: Se `SECURITY_SV.md`
- **GitHub Issues**: Rapportera problem i repositoryt
- **AWS Support**: Använd AWS Support Center för AWS-specifika frågor

---

**🎉 Grattis! Du har framgångsrikt distribuerat en produktionsklar fullstack-applikation på AWS!**

*Denna tutorial demonstrerar verkliga distributionsmönster som används av mjukvaruutvecklingsteam. De färdigheter du har lärt dig är direkt tillämpliga i professionella miljöer.*