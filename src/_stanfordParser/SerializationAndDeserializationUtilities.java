/*
 * @author  Vidhant (vidhant@gmail.com)
 */

package _stanfordParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

class SerializationAndDeserializationUtilities implements Serializable
{
	private static final long serialVersionUID = -9018889800500706499L;

	public static boolean Serialize(Document d, String serializedFilePath) throws IOException
	{
		FileOutputStream fout = null;
		ObjectOutputStream out = null;
		try
		{
			fout = new FileOutputStream(serializedFilePath);
			out = new ObjectOutputStream(fout);
			out.writeObject(d);
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		} finally
		{
			out.close();
			fout.close();
		}
	}

	public static void DeSerialize(Document d, String serializedFilePath) throws IOException
	{
		File f = new File(serializedFilePath);
		System.out.println(f.getAbsolutePath());
		FileInputStream fin = null;
		ObjectInputStream in = null;
		Document temp = null;
		try
		{
			fin = new FileInputStream(serializedFilePath);
			in = new ObjectInputStream(fin);
			temp = (Document) (in.readObject());
			d.sentences = temp.sentences;
			d.id = temp.id;
			d.documentPath = temp.documentPath;
			d.serializedDocumentPath = temp.serializedDocumentPath;
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			in.close();
			fin.close();
		}
	}
}