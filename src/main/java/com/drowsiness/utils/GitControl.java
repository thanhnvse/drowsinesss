package com.drowsiness.utils;
import java.io.*;
import java.nio.file.Paths;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.DetachedHeadException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;


public class GitControl {
    private String localPath = Paths.get("").toAbsolutePath().toString() + "/tddd";
    private String remotePath = "https://gitlab.com/datpro7703/tddd.git";
    private Repository localRepo;
    private Git git;
    private CredentialsProvider cp;
    private String name = "datpro7703@gmail.com";
    private String password = "datproo0399";

    public GitControl() throws IOException {
        this.localRepo = new FileRepository(localPath);
        cp = new UsernamePasswordCredentialsProvider(this.name, this.password);
        git = new Git(localRepo);
    }

    public void editText(float timeDetection, String description) throws  IOException {
        File file = new File(localPath + "/tmp.txt");
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("timeDetection=" + timeDetection);
        bw.newLine();
        bw.write("description=" + description);
        bw.newLine();
        bw.write("xxx=" + String.valueOf(StaticFuntion.getDate()));
        bw.flush();

        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        br.close();
        bw.close();
    }

    public void cleanDir() throws IOException {
        git.getRepository().close();
        git.close();
        File f = new File(localPath);
        FileUtils.deleteDirectory(f);
    }

    public void cloneRepo() throws IOException, NoFilepatternException, GitAPIException {
        git = Git.cloneRepository()
                .setURI(remotePath)
                .setDirectory(new File(localPath))
                .call();
    }

    public void addToRepo() throws IOException, NoFilepatternException, GitAPIException {
        AddCommand add = git.add();
        add.addFilepattern(".").call();
    }

    public void commitToRepo(String message) throws IOException, NoHeadException,
            NoMessageException, ConcurrentRefUpdateException,
            JGitInternalException, WrongRepositoryStateException, GitAPIException {
        git.commit().setMessage(message).call();
    }

    public void pushToRepo() throws IOException, JGitInternalException,
            InvalidRemoteException, GitAPIException {
        PushCommand pc = git.push();
        pc.setCredentialsProvider(cp)
                .setForce(true)
                .setPushAll();
        try {
            Iterator<PushResult> it = pc.call().iterator();
            if (it.hasNext()) {
                System.out.println(it.next().toString());
            }
        } catch (InvalidRemoteException e) {
            e.printStackTrace();
        }
    }

    public void pullFromRepo() throws IOException, WrongRepositoryStateException,
            InvalidConfigurationException, DetachedHeadException,
            InvalidRemoteException, CanceledException, RefNotFoundException,
            NoHeadException, GitAPIException {
        git.pull().call();
    }
}
