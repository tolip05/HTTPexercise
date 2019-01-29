package database.repositoris;

import database.enteties.User;
import database.util.RepositoryActionInvoker;
import database.util.RepositoryActionResult;
import database.util.RepositoryActionResultImpl;
import org.hibernate.TransactionException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public abstract class BaseRepository {
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY =
            Persistence.createEntityManagerFactory("casebook");



    private EntityTransaction entityTransaction;
    protected EntityManager entityManager;


    protected BaseRepository() {
    }

    private void initializeEntityManager() {
        this.entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
    }

    private void initializeTransaction() {
        this.entityTransaction = this.entityManager.getTransaction();
    }

    private void dismist() {
        this.entityManager.close();
    }

    protected RepositoryActionResult executeAction(RepositoryActionInvoker invoker){
        RepositoryActionResult actionResult = new RepositoryActionResultImpl();
        this.initializeEntityManager();
        this.initializeTransaction();

        try{
            this.entityTransaction.begin();
            invoker.invoke(actionResult);

            this.entityTransaction.commit();
        }catch (TransactionException e){
            if (this.entityTransaction != null && this.entityTransaction.isActive()){
                this.entityTransaction.rollback();
            }
            return null;
        }

        this.dismist();
        return actionResult;
    }

    public static void close(){
        ENTITY_MANAGER_FACTORY.close();
    }
}
