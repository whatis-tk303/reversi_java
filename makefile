# makefile for Reversi.java


TARGET = Reversi



JAVAC = javac



all: $(TARGET).class


clean:
	$(RM) -rf *.class *~

exec: $(TARGET).class
	java Reversi

%.class : %.java
	$(JAVAC) $<


# dependency
Reversi.class : Player.class InfoDialog.class
