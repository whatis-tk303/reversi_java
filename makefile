# makefile for Reversi.java


TARGET = Reversi



JAVAC = javac



all:
	$(JAVAC) Reversi.java


exec: $(TARGET).class
	java Reversi
