# Felsökningsguide

## 🔧 Vanliga problem och lösningar

### 1. VS Code Java Language Server-problem

**Symptom:**

- Röda slingriga linjer under Spring Boot-annotationer
- "Cannot find symbol"-fel för Spring-klasser
- Import-satser visas som inte hittade

**Grundorsak:**
VS Codes Java Language Server indexerar inte Maven-beroenden korrekt.

**Lösningar:**

#### Snabbfix:

1. Öppna Command Palette (`Cmd+Shift+P` på Mac, `Ctrl+Shift+P` på Windows/Linux)
2. Kör: `Java: Reload Projects`
3. Vänta på att Java Language Server återindexerar

#### Komplett uppdatering:

1. Kör uppdateringsskriptet:
   ```bash
   ./refresh-java.sh
   ```
2. I VS Code, kör: `Developer: Reload Window`

#### Nukleära alternativet:

1. Stäng VS Code helt
2. Ta bort `.vscode`-mappen i projektets rot
3. Ta bort `server/target`-mappen
4. Öppna VS Code igen och låt det återindexera projektet

### 2. GitHub Actions YAML-syntaxfel

**Symptom:**

- "Expected a scalar value, a sequence, or a mapping"-fel
- Arbetsflödet kan inte tolkas

**Lösning:**
Fixat i commit - YAML-arrayer korrekt formaterade:

```yaml
# Före (problematiskt):
branches: [ main ]

# Efter (fixat):
branches:
  - main
```

### 3. Maven build-problem

**Symptom:**

- Build misslyckas med beroendefel
- "No such file or directory: ./mvnw"

**Lösningar:**

#### Maven Wrapper-behörigheter:

```bash
chmod +x ./mvnw
./mvnw clean compile
```

#### Beroende-upplösning:

```bash
./mvnw dependency:resolve
./mvnw clean install
```

### 4. Databasanslutningsproblem

**Symptom:**

- Tester misslyckas med databasanslutningsfel
- Applikationsstart misslyckas

**Lösningar:**

#### Lokal PostgreSQL-installation:

```bash
# macOS
brew install postgresql
brew services start postgresql

# Skapa databas
psql postgres -c "CREATE DATABASE customerdb;"
psql postgres -c "CREATE USER customeruser WITH PASSWORD 'customerpass';"
psql postgres -c "GRANT ALL PRIVILEGES ON DATABASE customerdb TO customeruser;"
```

#### Kontrollera databasstatus:

```bash
brew services list | grep postgresql
psql -h localhost -U customeruser -d customerdb -c "SELECT 1;"
```

### 5. Frontend build-problem

**Symptom:**

- npm build misslyckas
- TypeScript-kompileringsfel

**Lösningar:**

#### Beroenden:

```bash
cd client
npm ci
npm run build
```

#### TypeScript-problem:

```bash
cd client
npx tsc --noEmit  # Kontrollera TypeScript-fel
npm run lint      # Kontrollera linting-problem
```

## 🧪 Verifieringskommandon

### Backend hälsokontroll:

```bash
cd server
./mvnw clean test
./mvnw spring-boot:run

# I en annan terminal:
curl http://localhost:8080/api/health
curl http://localhost:8080/api/customers
```

### Frontend hälsokontroll:

```bash
cd client
npm run build
npm run dev

# Besök: http://localhost:5173
```

### Full stack-test:

```bash
# Terminal 1 - Backend
cd server && ./mvnw spring-boot:run

# Terminal 2 - Frontend
cd client && npm run dev

# Terminal 3 - Testa API
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Test","lastName":"Användare"}'
```

## 🔍 Debug-kommandon

### Java Classpath-problem:

```bash
cd server
./mvnw dependency:tree
./mvnw dependency:analyze
```

### Spring Boot-debugging:

```bash
cd server
./mvnw spring-boot:run -Dspring.profiles.active=debug
```

### Nätverksanslutning:

```bash
# Kontrollera om portar är tillgängliga
lsof -i :8080  # Backend
lsof -i :5173  # Frontend
lsof -i :5432  # PostgreSQL
```

## 📋 Miljöverifiering

### Java-miljö:

```bash
java -version        # Bör vara 17+
echo $JAVA_HOME     # Bör peka på JDK 17+
mvn -version        # Bör använda JDK 17+
```

### Node-miljö:

```bash
node -version       # Bör vara 18+
npm -version        # Bör vara kompatibel med Node 18+
```

### Databasmiljö:

```bash
psql --version      # Bör vara PostgreSQL 14+
pg_isready          # Bör returnera "accepting connections"
```

## 🚨 När allt annat misslyckas

### Komplett projektåterställning:

```bash
# Backend
cd server
./mvnw clean
rm -rf target/
./mvnw install

# Frontend
cd ../client
rm -rf node_modules/
rm package-lock.json
npm install

# VS Code
# Stäng VS Code
rm -rf .vscode/
# Öppna VS Code igen
```

### Alternativ IDE:

Om VS Code fortsätter att ha problem, prova:

- IntelliJ IDEA Community Edition
- Eclipse med Spring Tools
- Kommandoradsutveckling med vim/nano

## 📞 Få hjälp

1. Kontrollera build-loggar i GitHub Actions
2. Verifiera att alla förkunskaper är installerade
3. Prova uppdateringsskriptet: `./refresh-java.sh`
4. Kontrollera denna felsökningsguide
5. Öppna ett issue med fullständiga felloggar och miljödetaljer