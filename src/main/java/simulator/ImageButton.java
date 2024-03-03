package simulator;

import java.awt.*;

public class ImageButton extends Button {
    private Image image;

    public ImageButton(Image image) {
        this.image = image;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }

    // Optional: You can add more constructors or methods as needed
}

