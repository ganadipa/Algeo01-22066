# Tubes 1 Aljabar Linear & Geometri: Sistem Persamaan Linier, Determinan, dan Aplikasinya [Eskrim]

## Table of contents

- <a href="#description">Deskripsi</a>
- <a href="#how-to-run">Cara Menggunakan Program</a>

<h2 id="description">Deskripsi</h2>
Tugas besar ini berisi program dalam bahasa Java yang berisi fungsi-fungsi eliminasi Gauss, eliminasi Gauss-Jordan, menentukan balikan matriks, menghitung determinan, kaidah Cramer (kaidah Cramer khusus untuk SPL dengan n peubah dan n persamaan), interpolasi polinomial, interpolasi bicubic, dan regresi linier berganda. Detail terkait program ini bisa dilihat di <a href="doc/Algeo01-13522066.pdf">file laporan</a>.

<h2 id="how-to-run">Cara Menggunakan Program</h2>

### _Compile & Run (Instant run)_

(Pastikan menggunakan git bash)
Untuk _run_ program ini menggunakan GUI lakukan:

```bash
sh compilerungui.sh
```

atau, jika ingin _run_ program ini dengan terminal, lakukan:

```bash
sh compilerun.sh
```

### _Compile_

_Compile_ program Java bisa dilakukan dengan cara (gunakan git bash):

```bash
sh compile.sh
```

Atau, untuk _compile_ program ke bytecode saja di folder bin bisa dengan:

```bash
cd src
javac -d ../bin GUI/Main.java
javac -d ../bin Main.java
cd ..
```

### _Run GUI_

Lalu, untuk _run_ GUI file .jar di folder bin bisa dengan double click path di bawah ini:

```bash
./bin/Matrix.jar
```

atau ketik

```bash
cd bin
java -jar Matrix.jar
cd ..
```

#### Bytecode

Untuk menjalankan program dari bytecode GUI bisa dengan:
(pastikan sudah compile)

```bash
sh rungui.sh
```

atau

```bash
sh run.sh
```

untuk menjalankan bytecode program melalui terminal.

#### Anggota Kelompok

- 13522066 - Nyoman Ganadipa Narayana
- 13522084 - Dhafin Fawwaz Ikramullah
- 13522117 - Mesach Harmasendro
