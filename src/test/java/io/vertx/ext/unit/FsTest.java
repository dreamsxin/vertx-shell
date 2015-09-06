package io.vertx.ext.unit;

import io.vertx.core.Vertx;
import io.vertx.ext.shell.command.BaseCommands;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.impl.vertx.FsHelper;
import io.vertx.ext.shell.registry.CommandRegistry;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@RunWith(VertxUnitRunner.class)
public class FsTest {

  File root;
  Vertx vertx;
  CommandRegistry registry;
  FsHelper helper;

  @Before
  public void before() throws Exception {
    File target = new File(System.getProperty("vertx.test.dir", "target"));
    root = Files.createTempDirectory(target.toPath(), "fs").toFile();
    System.setProperty("vertx.cwd", root.getAbsolutePath());
    vertx = Vertx.vertx();
    registry = CommandRegistry.get(vertx);
    helper = new FsHelper();
  }

  @After
  public void after() {
    System.clearProperty("vertx.cwd");
    registry.release();
    vertx.close();
  }

  @Test
  public void testCd(TestContext context) throws IOException {
    String dir_A = new File(root, "dir_A").getAbsolutePath();
    String file_B = new File(root, "file_B").getAbsolutePath();
    String dir_C = new File(root, "dir_C").getAbsolutePath();
    String dir_D = new File(new File(dir_C), "dir_D").getAbsolutePath();
    String file_E = new File(new File(dir_C), "file_E").getAbsolutePath();
    context.assertTrue(new File(dir_A).mkdir());
    context.assertTrue(new File(file_B).createNewFile());
    context.assertTrue(new File(dir_C).mkdir());
    context.assertTrue(new File(dir_D).mkdir());
    context.assertTrue(new File(file_E).createNewFile());
    helper.cd(vertx.fileSystem(), null, "dir_A", context.asyncAssertSuccess(path -> context.assertEquals(dir_A, path)));
    helper.cd(vertx.fileSystem(), null, "dir_A/", context.asyncAssertSuccess(path -> context.assertEquals(dir_A, path)));
    helper.cd(vertx.fileSystem(), null, "dir_A/.", context.asyncAssertSuccess(path -> context.assertEquals(dir_A, path)));
    helper.cd(vertx.fileSystem(), null, "./dir_A", context.asyncAssertSuccess(path -> context.assertEquals(dir_A, path)));
    helper.cd(vertx.fileSystem(), null, dir_A, context.asyncAssertSuccess(path -> context.assertEquals(dir_A, path)));
    helper.cd(vertx.fileSystem(), null, dir_A + "/", context.asyncAssertSuccess(path -> context.assertEquals(dir_A, path)));
    helper.cd(vertx.fileSystem(), null, "/", context.asyncAssertSuccess(path -> context.assertEquals("/", path)));
    helper.cd(vertx.fileSystem(), dir_C, "dir_D", context.asyncAssertSuccess(path -> context.assertEquals(dir_D, path)));
    helper.cd(vertx.fileSystem(), dir_C, "dir_D/", context.asyncAssertSuccess(path -> context.assertEquals(dir_D, path)));
    helper.cd(vertx.fileSystem(), dir_C, "dir_D/.", context.asyncAssertSuccess(path -> context.assertEquals(dir_D, path)));
    helper.cd(vertx.fileSystem(), dir_C, "./dir_D", context.asyncAssertSuccess(path -> context.assertEquals(dir_D, path)));
    helper.cd(vertx.fileSystem(), dir_C, "/", context.asyncAssertSuccess(path -> context.assertEquals("/", path)));
    helper.cd(vertx.fileSystem(), null, "file_B", context.asyncAssertFailure());
    helper.cd(vertx.fileSystem(), dir_C, "file_E", context.asyncAssertFailure());
    helper.cd(vertx.fileSystem(), null, "dir_", context.asyncAssertFailure());
    helper.cd(vertx.fileSystem(), null, "does_not_exists", context.asyncAssertFailure());
    helper.cd(vertx.fileSystem(), dir_C, "dir_", context.asyncAssertFailure());
    helper.cd(vertx.fileSystem(), dir_C, "does_not_exists", context.asyncAssertFailure());
  }

  @Test
  public void testLs(TestContext context) throws IOException {
    String dir_A = new File(root, "dir_A").getAbsolutePath();
    String file_B = new File(root, "file_B").getAbsolutePath();
    String dir_C = new File(root, "dir_C").getAbsolutePath();
    String dir_D = new File(new File(dir_C), "dir_D").getAbsolutePath();
    String file_E = new File(new File(dir_C), "file_E").getAbsolutePath();
    String file_F = new File(new File(dir_D), "file_F").getAbsolutePath();
    String file_G = new File(new File(dir_D), "file_G").getAbsolutePath();
    context.assertTrue(new File(dir_A).mkdir());
    context.assertTrue(new File(file_B).createNewFile());
    context.assertTrue(new File(dir_C).mkdir());
    context.assertTrue(new File(dir_D).mkdir());
    context.assertTrue(new File(file_E).createNewFile());
    context.assertTrue(new File(file_F).createNewFile());
    context.assertTrue(new File(file_G).createNewFile());
    helper.ls(vertx, null, ".", context.asyncAssertSuccess(files -> context.assertEquals(Arrays.asList(dir_A, dir_C, file_B), new ArrayList<>(files.keySet()))));
    helper.ls(vertx, null, "dir_C", context.asyncAssertSuccess(files -> context.assertEquals(Arrays.asList(dir_D, file_E), new ArrayList<>(files.keySet()))));
    helper.ls(vertx, null, "./dir_C", context.asyncAssertSuccess(files -> context.assertEquals(Arrays.asList(dir_D, file_E), new ArrayList<>(files.keySet()))));
    helper.ls(vertx, null, "./dir_C/..", context.asyncAssertSuccess(files -> context.assertEquals(Arrays.asList(dir_A, dir_C, file_B), new ArrayList<>(files.keySet()))));
    helper.ls(vertx, root.getAbsolutePath(), ".", context.asyncAssertSuccess(files -> context.assertEquals(Arrays.asList(dir_A, dir_C, file_B), new ArrayList<>(files.keySet()))));
    helper.ls(vertx, root.getAbsolutePath(), dir_C, context.asyncAssertSuccess(files -> context.assertEquals(Arrays.asList(dir_D, file_E), new ArrayList<>(files.keySet()))));
    helper.ls(vertx, root.getAbsolutePath(), "dir_C/dir_D", context.asyncAssertSuccess(files -> context.assertEquals(Arrays.asList(file_F, file_G), new ArrayList<>(files.keySet()))));
    helper.ls(vertx, root.getAbsolutePath(), "./dir_C/dir_D", context.asyncAssertSuccess(files -> context.assertEquals(Arrays.asList(file_F, file_G), new ArrayList<>(files.keySet()))));
  }

  @Test
  public void complete(TestContext context) throws Exception {
    String foo11 = new File(root, "foo11").getAbsolutePath();
    String foo22 = new File(root, "foo22").getAbsolutePath();
    String foo33 = new File(root, "foo33").getAbsolutePath();
    String bar11 = new File(foo11, "bar11").getAbsolutePath();
    String bar22 = new File(foo11, "bar22").getAbsolutePath();
    String bar33 = new File(foo11, "bar33").getAbsolutePath();
    context.assertTrue(new File(foo11).mkdir());
    context.assertTrue(new File(foo22).mkdir());
    context.assertTrue(new File(foo33).createNewFile());
    context.assertTrue(new File(bar11).mkdir());
    context.assertTrue(new File(bar22).mkdir());
    context.assertTrue(new File(bar33).createNewFile());
    helper.complete(vertx, null, "foo11", context.asyncAssertSuccess(result -> {
      context.assertEquals(Collections.singletonMap("/", false), result);
    }));
    helper.complete(vertx, null, "foo1", context.asyncAssertSuccess(result -> {
      context.assertEquals(Collections.singletonMap("1/", false), result);
    }));
    helper.complete(vertx, null, "foo", context.asyncAssertSuccess(result -> {
      Map<String, Boolean> expected = new HashMap<>();
      expected.put("foo11/", false);
      expected.put("foo22/", false);
      expected.put("foo33", true);
      context.assertEquals(expected, result);
    }));
    helper.complete(vertx, null, "", context.asyncAssertSuccess(result -> {
      Map<String, Boolean> expected = new HashMap<>();
      expected.put("foo", false);
      context.assertEquals(expected, result);
    }));
    helper.complete(vertx, null, "foo11/", context.asyncAssertSuccess(result -> {
      Map<String, Boolean> expected = new HashMap<>();
      expected.put("bar", false);
      context.assertEquals(expected, result);
    }));
    helper.complete(vertx, null, "foo11/bar", context.asyncAssertSuccess(result -> {
      Map<String, Boolean> expected = new HashMap<>();
      expected.put("bar11/", false);
      expected.put("bar22/", false);
      expected.put("bar33", true);
      context.assertEquals(expected, result);
    }));
  }
}
