package com.vackosar.gitproblemnotifier.boundary;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.vackosar.gitproblemnotifier.control.SshTrasportCallback;
import com.vackosar.gitproblemnotifier.entity.Arguments;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Module extends AbstractModule {

    private final String[] args;

    public Module(String[] args) {
        this.args = args;
    }

    @Provides @Singleton
    public Git provideGit(Path workDir, Arguments arguments, SshTrasportCallback callback) throws GitAPIException {
        try {
            final FileRepositoryBuilder builder = new FileRepositoryBuilder();
            final FileRepositoryBuilder gitDir = builder.findGitDir(workDir.toFile());
            if (gitDir == null) {
                throw new IllegalArgumentException("Git repository root directory not found ascending from current working directory:'" + workDir + "'.");
            }
            Git git = Git.wrap(builder.build());
            if (arguments.key.isPresent()) {
                fetch(git, callback);
            }
            return git;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void fetch(Git git, SshTrasportCallback callback) throws GitAPIException {
        git
            .fetch()
            .setTransportConfigCallback(callback)
            .call();
    }

    @Provides @Singleton
    public Path provideWorkDir() {
        return Paths.get(System.getProperty("user.dir"));
    }

    @Provides @Singleton
    public Arguments provideArguments() {return new Arguments(args);}

    @Override
    protected void configure() {}
}
