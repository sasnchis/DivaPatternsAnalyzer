package Utility.ReadersAndWriters.ReadDSC;

import java.io.*;

public class StreamReader {
    private File targetFile;
    protected DataInputStream stream;
    protected BufferedInputStream reader;

    public StreamReader(File file) throws IOException {
        if (!file.exists()) throw new FileNotFoundException();
        targetFile = file;
        reader = new BufferedInputStream(new FileInputStream(targetFile));
        stream = new DataInputStream(reader);
    }

    public StreamReader(byte[] bytes) {
        stream = new DataInputStream(new ByteArrayInputStream(bytes));
    }

    public String read4Bytes() throws IOException {
        return readNBytes(4);
    }

    public String read8Bytes() throws IOException {
        return readNBytes(8);
    }

    public String read16Bytes() throws IOException {
        return readNBytes(16);
    }

    public String read32Bytes() throws IOException {
        return readNBytes(32);
    }

    public String read64Bytes() throws IOException {
        return readNBytes(64);
    }

    public String readNBytes(int count) throws IOException {
        if (count <= 0) return null;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++)
            builder.insert(0, read1Byte());
        return builder.toString();
    }

    public String read1Byte() throws IOException {
        int read = stream.readUnsignedByte();
        String out = Long.toBinaryString(read);
        out = "0".repeat(8 - out.length()) + out;
        return out;
    }

    public long readUnsignedInt() throws IOException {
        return Long.parseUnsignedLong(read4Bytes(), 2);
    }

    public long readInt() throws IOException {
        String input = read4Bytes();
        long out = Long.parseLong(input, 2);
        long checker = out & 0x80000000;
        if (checker > 0) {
            int test = Integer.parseInt(input.substring(1), 2);
            test += Integer.MAX_VALUE + 1;
            return test;
        }
        return out;
    }

    public String getPath() {
        return targetFile.getAbsolutePath();
    }

    public File getFile() {
        return targetFile;
    }
}
