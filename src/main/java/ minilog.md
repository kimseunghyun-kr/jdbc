2023 / 07 / 17: --> up to MemberRepositoryV2

jdbc transactions. issue with this that both
'jdbc reliant code and exceptions spill over to 
service which is best kept as a POJO based class. 
Next will investigate how PlatformTransactionManager 
interface provided by spring, along with a @Repository layer
can keep the open close principle,(thus preventing the
aforementioned issue), through DI(OOP principles)
