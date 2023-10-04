/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ebingk;

/**
 *
 * @author BIBIN RIJO
 */

import java.util.*;

enum Command {
    MOVE, TURN_LEFT, TURN_RIGHT
}

class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }
}

class Grid {
    private final int width;
    private final int height;
    private final Set<Position> obstacles = new HashSet<>();

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void addObstacle(int x, int y) {
        obstacles.add(new Position(x, y));
    }

    public boolean hasObstacle(int x, int y) {
        return obstacles.contains(new Position(x, y));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

interface RoverCommand {
    void execute(Rover rover);
}

class MoveCommand implements RoverCommand {
    @Override
    public void execute(Rover rover) {
        rover.move();
    }
}

class TurnLeftCommand implements RoverCommand {
    @Override
    public void execute(Rover rover) {
        rover.turnLeft();
    }
}

class TurnRightCommand implements RoverCommand {
    @Override
    public void execute(Rover rover) {
        rover.turnRight();
    }
}

class Rover {
    private int x;
    private int y;
    private Direction direction;
    private final Grid grid;

    public Rover(int x, int y, Direction direction, Grid grid) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.grid = grid;
    }

    public void move() {
        int newX = x;
        int newY = y;
        switch (direction) {
            case N:
                newY++;
                break;
            case S:
                newY--;
                break;
            case E:
                newX++;
                break;
            case W:
                newX--;
                break;
        }

        if (isValidMove(newX, newY)) {
            x = newX;
            y = newY;
        }
    }

    public void turnLeft() {
        direction = direction.left();
    }

    public void turnRight() {
        direction = direction.right();
    }

    private boolean isValidMove(int newX, int newY) {
        return newX >= 0 && newX < grid.getWidth() && newY >= 0 && newY < grid.getHeight()
                && !grid.hasObstacle(newX, newY);
    }

    public String getStatusReport() {
        return "Rover is at (" + x + ", " + y + ") facing " + direction + ".";
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Direction getDirection() {
        return direction;
    }
}

enum Direction {
    N, E, S, W;

    public Direction left() {
        int ordinal = (this.ordinal() + 3) % 4;
        return Direction.values()[ordinal];
    }

    public Direction right() {
        int ordinal = (this.ordinal() + 1) % 4;
        return Direction.values()[ordinal];
    }
}

public class MarsRoverSimulation {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the grid width: ");
        int gridWidth = scanner.nextInt();

        System.out.print("Enter the grid height: ");
        int gridHeight = scanner.nextInt();

        Grid grid = new Grid(gridWidth, gridHeight);

        System.out.print("Enter the number of obstacles: ");
        int numObstacles = scanner.nextInt();
        for (int i = 0; i < numObstacles; i++) {
            System.out.print("Enter obstacle x-coordinate: ");
            int obstacleX = scanner.nextInt();
            System.out.print("Enter obstacle y-coordinate: ");
            int obstacleY = scanner.nextInt();
            grid.addObstacle(obstacleX, obstacleY);
        }

        System.out.print("Enter the initial x-coordinate of the rover: ");
        int initialX = scanner.nextInt();
        System.out.print("Enter the initial y-coordinate of the rover: ");
        int initialY = scanner.nextInt();
        System.out.print("Enter the initial direction (N, S, E, W) of the rover: ");
        String initialDirectionStr = scanner.next();
        Direction initialDirection = Direction.valueOf(initialDirectionStr.toUpperCase());

        Rover rover = new Rover(initialX, initialY, initialDirection, grid);

        System.out.print("Enter the commands for the rover: ");
        String commands = scanner.next();

        for (char command : commands.toCharArray()) {
            RoverCommand roverCommand = null;
            switch (command) {
                case 'M':
                    roverCommand = new MoveCommand();
                    break;
                case 'L':
                    roverCommand = new TurnLeftCommand();
                    break;
                case 'R':
                    roverCommand = new TurnRightCommand();
                    break;
            }

            if (roverCommand != null) {
                roverCommand.execute(rover);
            }
        }

        String finalPosition = "Final Position: (" + rover.getX() + ", " + rover.getY() + ", " + rover.getDirection() + ")";
        String statusReport = rover.getStatusReport();

        System.out.println(finalPosition);
        System.out.println(statusReport);

        scanner.close();
    }
}
