# First Bank Uganda - Account Opening Application

## Project Overview
Desktop Java Swing application for opening new bank accounts as per the coursework requirements.

## Features Implemented
- Full Object-Oriented Design (Abstract Account class + 5 subclasses with polymorphism)
- Dynamic Date of Birth (auto-updates days, supports leap years)
- Comprehensive Input Validation
- Account Number Generation (e.g., KLA-2026-000001)
- Data Persistence to MS Access Database
- Error handling and user feedback

## How to Run
1. Ensure `FirstBankDB.accdb` is in project root.
2. UCanAccess JARs are in `lib/` folder.
3. Compile:
   ```powershell
   javac -cp "lib/*;src" -d bin src\gui\*.java src\model\*.java src\util\*.java
