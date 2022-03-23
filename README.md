# MSSQL implicit data type conversion

In this repository you can find sample use-cases that shows how MSSQL converts data-type when used by `UNION ALL` statement is used.

1. Run `docker compose up` command to start MSSQL instance with sample DB structure
2. Run `MainApplication` Spring Boot application to see use-cases and their output

```
 >>> Convert dates to OffsetDateTime <<<
  >> promotes date-times to c_datetimeoffset at offset +00:00
0.	{c_datetime2=3294-05-17 09:11:21 +02:00, c_datetime=3294-05-17 09:11:21 +02:00, c_smalldatetime=3294-05-17 09:11:21 +02:00}
1.	{c_datetime2=null, c_datetime=null, c_smalldatetime=null}
2.	{c_datetime2=4005-04-26 17:31:54 +02:00, c_datetime=4005-04-26 17:31:54 +02:00, c_smalldatetime=4005-04-26 17:31:54 +02:00}
3.	{c_datetime2=5904-10-21 19:31:40 +02:00, c_datetime=5904-10-21 19:31:40 +02:00, c_smalldatetime=5904-10-21 19:31:40 +02:00}
4.	{c_datetime2=6364-12-15 05:19:53 +01:00, c_datetime=6364-12-15 05:19:53 +01:00, c_smalldatetime=6364-12-15 05:19:53 +01:00}
5.	{c_datetime2=3851-11-13 22:50:52 +01:00, c_datetime=3851-11-13 22:50:52 +01:00, c_smalldatetime=3851-11-13 22:50:52 +01:00}
6.	{c_datetime2=null, c_datetime=null, c_smalldatetime=null}
7.	{c_datetime2=5606-02-22 16:10:57 +01:00, c_datetime=5606-02-22 16:10:57 +01:00, c_smalldatetime=5606-02-22 16:10:57 +01:00}
8.	{c_datetime2=9628-03-18 19:22:53 +01:00, c_datetime=9628-03-18 19:22:53 +01:00, c_smalldatetime=9628-03-18 19:22:53 +01:00}
9.	{c_datetime2=null, c_datetime=null, c_smalldatetime=null}
10.	{c_datetime2=null, c_datetime=5821-07-31 13:02:01 +00:00, c_smalldatetime=null}
11.	{c_datetime2=3328-05-16 20:18:04 +00:00, c_datetime=null, c_smalldatetime=2073-01-18 11:22:00 +00:00}
12.	{c_datetime2=7135-01-04 22:11:48 +00:00, c_datetime=5342-07-02 02:50:51 +00:00, c_smalldatetime=null}
13.	{c_datetime2=null, c_datetime=null, c_smalldatetime=2068-10-19 02:41:00 +00:00}
14.	{c_datetime2=null, c_datetime=null, c_smalldatetime=1957-06-04 09:53:00 +00:00}
15.	{c_datetime2=null, c_datetime=4205-07-03 06:08:44 +00:00, c_smalldatetime=2026-06-08 14:59:00 +00:00}
16.	{c_datetime2=null, c_datetime=1891-06-20 22:54:32 +00:00, c_smalldatetime=null}
17.	{c_datetime2=null, c_datetime=5348-03-05 07:09:31 +00:00, c_smalldatetime=null}
18.	{c_datetime2=6826-04-08 21:35:29 +00:00, c_datetime=null, c_smalldatetime=null}
19.	{c_datetime2=null, c_datetime=null, c_smalldatetime=null}
```
---
```
 >>> Convert Date & Time to OffsetDateTime <<<
  >> promotes date to hour 00:00:00 at offset +00:00
  >> promotes time to year 1900-01-01 at offset +00:00
0.	{d1=3294-05-17 09:11:21 +02:00, d2=3294-05-17 09:11:21 +02:00}
1.	{d1=null, d2=null}
2.	{d1=4005-04-26 17:31:54 +02:00, d2=4005-04-26 17:31:54 +02:00}
3.	{d1=5904-10-21 19:31:40 +02:00, d2=5904-10-21 19:31:40 +02:00}
4.	{d1=6364-12-15 05:19:53 +01:00, d2=6364-12-15 05:19:53 +01:00}
5.	{d1=3851-11-13 22:50:52 +01:00, d2=3851-11-13 22:50:52 +01:00}
6.	{d1=null, d2=null}
7.	{d1=5606-02-22 16:10:57 +01:00, d2=5606-02-22 16:10:57 +01:00}
8.	{d1=9628-03-18 19:22:53 +01:00, d2=9628-03-18 19:22:53 +01:00}
9.	{d1=null, d2=null}
10.	{d1=null, d2=null}
11.	{d1=8343-06-19 00:00:00 +00:00, d2=null}
12.	{d1=null, d2=null}
13.	{d1=4663-07-10 00:00:00 +00:00, d2=1900-01-01 17:14:32 +00:00}
14.	{d1=null, d2=null}
15.	{d1=9428-02-04 00:00:00 +00:00, d2=null}
16.	{d1=0438-11-09 00:00:00 +00:00, d2=1900-01-01 03:53:52 +00:00}
17.	{d1=6288-08-22 00:00:00 +00:00, d2=null}
18.	{d1=2987-03-06 00:00:00 +00:00, d2=null}
19.	{d1=null, d2=null}
```
---
```
 >>> Convert Date & Time to each other <<<
  >> Unable to UNION ALL columns date & time with each other: Operand type clash: time is incompatible with date
```
---
```
 >>> Convert Date & Time to datetime data type <<<
  >> date: range 0001-01-01 through 9999-12-31
  >> datetime: Date range - January 1, 1753, through December 31, 9999
  >>           Time range - 00:00:00 through 23:59:59.997
  >> Unable to UNION ALL columns date & time to datetime data type: The conversion of a date data type to a datetime data type resulted in an out-of-range value.
```
---
```
 >>> Convert Date & Time to datetime2 data type (bigger values) <<<
  >> promotes date to hour 00:00:00
  >> promotes time to year 1900-01-01
0.	{d1=7977-09-08 16:00:20.0, d2=7977-09-08 16:00:20.0}
1.	{d1=null, d2=null}
2.	{d1=9708-05-09 10:39:05.0, d2=9708-05-09 10:39:05.0}
3.	{d1=8333-01-21 06:38:43.0, d2=8333-01-21 06:38:43.0}
4.	{d1=7897-12-06 20:49:29.0, d2=7897-12-06 20:49:29.0}
5.	{d1=null, d2=null}
6.	{d1=7106-08-18 18:26:30.0, d2=7106-08-18 18:26:30.0}
7.	{d1=null, d2=null}
8.	{d1=6375-01-19 11:28:35.0, d2=6375-01-19 11:28:35.0}
9.	{d1=9538-05-24 05:17:31.0, d2=9538-05-24 05:17:31.0}
10.	{d1=null, d2=null}
11.	{d1=8343-06-19 00:00:00.0, d2=null}
12.	{d1=null, d2=null}
13.	{d1=4663-07-10 00:00:00.0, d2=1900-01-01 17:14:32.0}
14.	{d1=null, d2=null}
15.	{d1=9428-02-04 00:00:00.0, d2=null}
16.	{d1=0438-11-09 00:00:00.0, d2=1900-01-01 03:53:52.0}
17.	{d1=6288-08-22 00:00:00.0, d2=null}
18.	{d1=2987-03-06 00:00:00.0, d2=null}
19.	{d1=null, d2=null}
```
---
```
 >>> Convert Date to datetime data type <<<
  >> date: range 0001-01-01 through 9999-12-31
  >> datetime: Date range - January 1, 1753, through December 31, 9999
  >>           Time range - 00:00:00 through 23:59:59.997
  >> Unable to UNION ALL columns date & datetime with each other: The conversion of a date data type to a datetime data type resulted in an out-of-range value.
```
---
```
 >>> Convert numbers to datetime2 <<<
 Unable to UNION ALL column c_float with date-time: Operand type clash: float is incompatible with datetime2
 Unable to UNION ALL column c_real with date-time: Operand type clash: real is incompatible with datetime2
 Unable to UNION ALL column c_decimal with date-time: Operand type clash: decimal is incompatible with datetime2
 Unable to UNION ALL column c_money with date-time: Operand type clash: money is incompatible with datetime2
 Unable to UNION ALL column c_smallmoney with date-time: Operand type clash: smallmoney is incompatible with datetime2
 Unable to UNION ALL column c_bigint with date-time: Operand type clash: bigint is incompatible with datetime2
 Unable to UNION ALL column c_int with date-time: Operand type clash: int is incompatible with datetime2
 Unable to UNION ALL column c_smallint with date-time: Operand type clash: smallint is incompatible with datetime2
 Unable to UNION ALL column c_tinyint with date-time: Operand type clash: tinyint is incompatible with datetime2
 Unable to UNION ALL column c_bit with date-time: Operand type clash: bit is incompatible with datetime2
```
---
```
 >>> Convert numbers to float <<<
  >> promotes to float 0.0, 3.14, 1.0
0.	{d1=null, d2=null, d3=null, d4=null, d5=null, d6=null, d7=null, d8=null, d9=null}
1.	{d1=null, d2=null, d3=null, d4=null, d5=null, d6=null, d7=null, d8=null, d9=null}
2.	{d1=60.108238220214844, d2=60.108238220214844, d3=60.108238220214844, d4=60.108238220214844, d5=60.108238220214844, d6=60.108238220214844, d7=60.108238220214844, d8=60.108238220214844, d9=60.108238220214844}
3.	{d1=null, d2=null, d3=null, d4=null, d5=null, d6=null, d7=null, d8=null, d9=null}
4.	{d1=-2.4962425231933594, d2=-2.4962425231933594, d3=-2.4962425231933594, d4=-2.4962425231933594, d5=-2.4962425231933594, d6=-2.4962425231933594, d7=-2.4962425231933594, d8=-2.4962425231933594, d9=-2.4962425231933594}
5.	{d1=22.221534729003906, d2=22.221534729003906, d3=22.221534729003906, d4=22.221534729003906, d5=22.221534729003906, d6=22.221534729003906, d7=22.221534729003906, d8=22.221534729003906, d9=22.221534729003906}
6.	{d1=null, d2=null, d3=null, d4=null, d5=null, d6=null, d7=null, d8=null, d9=null}
7.	{d1=49.2795295715332, d2=49.2795295715332, d3=49.2795295715332, d4=49.2795295715332, d5=49.2795295715332, d6=49.2795295715332, d7=49.2795295715332, d8=49.2795295715332, d9=49.2795295715332}
8.	{d1=-85.87462615966797, d2=-85.87462615966797, d3=-85.87462615966797, d4=-85.87462615966797, d5=-85.87462615966797, d6=-85.87462615966797, d7=-85.87462615966797, d8=-85.87462615966797, d9=-85.87462615966797}
9.	{d1=-79.98884582519531, d2=-79.98884582519531, d3=-79.98884582519531, d4=-79.98884582519531, d5=-79.98884582519531, d6=-79.98884582519531, d7=-79.98884582519531, d8=-79.98884582519531, d9=-79.98884582519531}
10.	{d1=null, d2=862894.0, d3=-436185.541, d4=3.2174, d5=null, d6=9.9068369E7, d7=null, d8=null, d9=null}
11.	{d1=null, d2=null, d3=null, d4=null, d5=null, d6=5.45645599E8, d7=-28612.0, d8=null, d9=null}
12.	{d1=null, d2=756712.0, d3=null, d4=0.7811, d5=8.7521849017772877E17, d6=2.7821279E7, d7=null, d8=11.0, d9=0.0}
13.	{d1=null, d2=96044.0, d3=-573234.3737, d4=null, d5=null, d6=null, d7=4691.0, d8=24.0, d9=0.0}
14.	{d1=null, d2=437613.0, d3=284560.8539, d4=-55.4645, d5=2.6970966384362778E18, d6=null, d7=32651.0, d8=null, d9=0.0}
15.	{d1=null, d2=null, d3=null, d4=null, d5=-3.5859167129202391E18, d6=null, d7=null, d8=null, d9=null}
16.	{d1=null, d2=-951617.0, d3=-811955.0891, d4=null, d5=-2.03975673987328512E18, d6=-7.32694353E8, d7=null, d8=null, d9=null}
17.	{d1=null, d2=433505.0, d3=null, d4=21.3313, d5=-9.1210171881410778E17, d6=-1.815432152E9, d7=16654.0, d8=null, d9=1.0}
18.	{d1=null, d2=null, d3=null, d4=null, d5=4.1334729381104788E18, d6=-6.40231426E8, d7=28539.0, d8=27.0, d9=null}
19.	{d1=6.566399574279785, d2=-864038.0, d3=-405428.2954, d4=-68.0912, d5=null, d6=1.555466879E9, d7=-28369.0, d8=null, d9=null}
```
---
```
 >>> Convert numbers to int <<<
  >> promotes to float 0, 3, 1
0.	{d5=null, d6=null, d7=null, d8=null, d9=null}
1.	{d5=-4758063336754399447, d6=-4758063336754399447, d7=-4758063336754399447, d8=-4758063336754399447, d9=-4758063336754399447}
2.	{d5=8361874209175057792, d6=8361874209175057792, d7=8361874209175057792, d8=8361874209175057792, d9=8361874209175057792}
3.	{d5=-2328047158025157230, d6=-2328047158025157230, d7=-2328047158025157230, d8=-2328047158025157230, d9=-2328047158025157230}
4.	{d5=null, d6=null, d7=null, d8=null, d9=null}
5.	{d5=8300195030075082474, d6=8300195030075082474, d7=8300195030075082474, d8=8300195030075082474, d9=8300195030075082474}
6.	{d5=861632955444504846, d6=861632955444504846, d7=861632955444504846, d8=861632955444504846, d9=861632955444504846}
7.	{d5=null, d6=null, d7=null, d8=null, d9=null}
8.	{d5=null, d6=null, d7=null, d8=null, d9=null}
9.	{d5=-1923436615246673430, d6=-1923436615246673430, d7=-1923436615246673430, d8=-1923436615246673430, d9=-1923436615246673430}
10.	{d5=null, d6=99068369, d7=null, d8=null, d9=null}
11.	{d5=null, d6=545645599, d7=-28612, d8=null, d9=null}
12.	{d5=875218490177728707, d6=27821279, d7=null, d8=11, d9=0}
13.	{d5=null, d6=null, d7=4691, d8=24, d9=0}
14.	{d5=2697096638436277673, d6=null, d7=32651, d8=null, d9=0}
15.	{d5=-3585916712920238972, d6=null, d7=null, d8=null, d9=null}
16.	{d5=-2039756739873284997, d6=-732694353, d7=null, d8=null, d9=null}
17.	{d5=-912101718814107769, d6=-1815432152, d7=16654, d8=null, d9=1}
18.	{d5=4133472938110478792, d6=-640231426, d7=28539, d8=27, d9=null}
19.	{d5=null, d6=1555466879, d7=-28369, d8=null, d9=null}
```
---
```
 >>> Convert strings <<<
  >> promotes to ntext
0.	{d1=MQ_ybNZ(PR, d2=MQ_ybNZ(PR, d3=MQ_ybNZ(PR, d4=MQ_ybNZ(PR, d5=MQ_ybNZ(PR}
1.	{d1=.K]?P]Wd ;.c$b7cfwlkMcj", d2=.K]?P]Wd ;.c$b7cfwlkMcj", d3=.K]?P]Wd ;.c$b7cfwlkMcj", d4=.K]?P]Wd ;.c$b7cfwlkMcj", d5=.K]?P]Wd ;.c$b7cfwlkMcj"}
2.	{d1=null, d2=null, d3=null, d4=null, d5=null}
3.	{d1=0o?u]Rz4Z;Te}A8M]?[nL"svR."Rd*BWhfW\CJ9rPZY+P]!S8XT~NGS}=;, d2=0o?u]Rz4Z;Te}A8M]?[nL"svR."Rd*BWhfW\CJ9rPZY+P]!S8XT~NGS}=;, d3=0o?u]Rz4Z;Te}A8M]?[nL"svR."Rd*BWhfW\CJ9rPZY+P]!S8XT~NGS}=;, d4=0o?u]Rz4Z;Te}A8M]?[nL"svR."Rd*BWhfW\CJ9rPZY+P]!S8XT~NGS}=;, d5=0o?u]Rz4Z;Te}A8M]?[nL"svR."Rd*BWhfW\CJ9rPZY+P]!S8XT~NGS}=;}
4.	{d1=null, d2=null, d3=null, d4=null, d5=null}
5.	{d1=t(<IRS9aF_A$ALEN7{7;peb)\2grt(]lW, d2=t(<IRS9aF_A$ALEN7{7;peb)\2grt(]lW, d3=t(<IRS9aF_A$ALEN7{7;peb)\2grt(]lW, d4=t(<IRS9aF_A$ALEN7{7;peb)\2grt(]lW, d5=t(<IRS9aF_A$ALEN7{7;peb)\2grt(]lW}
6.	{d1=b"tK<ClAt]]MCN``cMAwJd_LDy1<*c/k&i2)Ks];6T%, d2=b"tK<ClAt]]MCN``cMAwJd_LDy1<*c/k&i2)Ks];6T%, d3=b"tK<ClAt]]MCN``cMAwJd_LDy1<*c/k&i2)Ks];6T%, d4=b"tK<ClAt]]MCN``cMAwJd_LDy1<*c/k&i2)Ks];6T%, d5=b"tK<ClAt]]MCN``cMAwJd_LDy1<*c/k&i2)Ks];6T%}
7.	{d1=X]1m)s}h5&, d2=X]1m)s}h5&, d3=X]1m)s}h5&, d4=X]1m)s}h5&, d5=X]1m)s}h5&}
8.	{d1=!vW^*U;&dg6Ad]YUlVb2&$d'EDy6r@tu`IU, d2=!vW^*U;&dg6Ad]YUlVb2&$d'EDy6r@tu`IU, d3=!vW^*U;&dg6Ad]YUlVb2&$d'EDy6r@tu`IU, d4=!vW^*U;&dg6Ad]YUlVb2&$d'EDy6r@tu`IU, d5=!vW^*U;&dg6Ad]YUlVb2&$d'EDy6r@tu`IU}
9.	{d1=k4hs^OH$b(n$k<8qV_Y"4klQtx, d2=k4hs^OH$b(n$k<8qV_Y"4klQtx, d3=k4hs^OH$b(n$k<8qV_Y"4klQtx, d4=k4hs^OH$b(n$k<8qV_Y"4klQtx, d5=k4hs^OH$b(n$k<8qV_Y"4klQtx}
10.	{d1=65d7q_V,eUn^'Io<$Q9IVyr[X /9zhd.w=g, d2=0N$hc0)T5-.T(>*r7fjOsW3N8HdxKwn2gl0;, d3=j, d4=Md:D4CGJV/"<S>;1N}#O6W4, d5=}}
11.	{d1=TdB*-0}R)JyQ@@y0|#, d2=aGP!e J8@TwHPwm9<6w;<1~u]<V4/hnR&J>IB23|_6o)wPl;7@H`;X%_, d3=N, d4=null, d5=null}
12.	{d1=null, d2=p%&Q4er[nT`Ol1sRa: \I/vzE~q/jJkUwI}^S#pK;lD\=HdW03Eg{]{=&d^^iX', d3=null, d4=null, d5=null}
13.	{d1=null, d2=!nMnA)+C?ZU;CBzhr/$snS<nFHCa@Y}fg7)HtN&n, d3=0, d4=null, d5=null}
14.	{d1=9!f='I`4bBVHX"]<yW_5nzw*wdnhJQ8)wEH%n+~QHUw~524N5"3x%, d2=V[7?c.!nYff{VRJ4'Pr!f5:/42z0, d3=y, d4=rb_xK"dV:l, d5=&}
15.	{d1=null, d2=null, d3=null, d4=null, d5=null}
16.	{d1=LTU$oe/`d8W]\3Go-3, d2=null, d3=r, d4=null, d5=null}
17.	{d1=k]-q7!U(rB]%)ay|k-.mJM'z2<Hxz6)o=o<\&l>[Ypt5LcH, d2=null, d3=null, d4=null, d5=%}
18.	{d1=dyJlK;{ ^:'9qfYezMV%r&1HP:(X@fvr f0*, d2=+nZz{S.N0YE2LQnnq<BjFYEL?wvU%YiEvk~Ef1%i,EkavL;oZ(%M\lW]Z|, d3=null, d4=\DU!n$E$4~XI!wCQI&<GY*%PNH@<':FleU|R"oa~quw?&--]Bg\`rW*a, d5=null}
19.	{d1=null, d2=null, d3=[, d4=/j5'ZuM?%hT)bP, d5=null}
```
---
```
 >>> Convert binary <<<
  >> promotes to byte[]/varbinary
0.	{d1=[B@44536de4, d2=[B@5fcfde70}
1.	{d1=[B@4d95a72e, d2=[B@28da7d11}
2.	{d1=null, d2=null}
3.	{d1=[B@77b919a3, d2=[B@5624657a}
4.	{d1=[B@36681447, d2=[B@4d21c56e}
5.	{d1=[B@726aa968, d2=[B@7100dea}
6.	{d1=null, d2=null}
7.	{d1=[B@712cfb63, d2=[B@32e54a9d}
8.	{d1=[B@15639440, d2=[B@121bb45b}
9.	{d1=[B@4faa298, d2=[B@1cd3b138}
10.	{d1=[B@151bf776, d2=[B@5a6d30e2}
11.	{d1=[B@b52b755, d2=[B@a098d76}
12.	{d1=null, d2=null}
13.	{d1=null, d2=null}
14.	{d1=[B@40e37b06, d2=[B@733aa9d8}
15.	{d1=[B@6dcc40f5, d2=[B@2b680207}
16.	{d1=[B@70887727, d2=[B@56da7487}
17.	{d1=null, d2=null}
18.	{d1=null, d2=null}
19.	{d1=[B@599e4d41, d2=[B@328d044f}
```
