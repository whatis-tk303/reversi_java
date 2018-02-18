# makefile for Reversi.java


TARGET = Reversi



JAVAC = javac



all:
	$(JAVAC) Reversi.java



clean:
	$(RM) -rf *.class *~

exec: $(TARGET).class
	java Reversi
